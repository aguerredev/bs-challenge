package com.bestseller.starbux.services.impl;

import com.bestseller.starbux.domain.Beverage;
import com.bestseller.starbux.domain.dto.CartDTO;
import com.bestseller.starbux.domain.dto.DrinkDTO;
import com.bestseller.starbux.domain.dto.OrderDTO;
import com.bestseller.starbux.domain.dto.OrdersPerCustomerDTO;
import com.bestseller.starbux.domain.dto.ToppingDTO;
import com.bestseller.starbux.domain.dto.UserDTO;
import com.bestseller.starbux.entities.Order;
import com.bestseller.starbux.exceptions.CartNotFoundException;
import com.bestseller.starbux.mapper.OrderMapper;
import com.bestseller.starbux.repositories.OrderRepository;
import com.bestseller.starbux.services.OrderDetailService;
import com.bestseller.starbux.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service to have business logic for Order specific things.
 */
@Service
public class DefaultOrderService implements OrderService {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultOrderService.class);
    private final String EMPTY = "";
    private int cartTotalToGet25PercentDiscount;
    private int itemsToGetCheaperOneFree;

    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;

    private Map<UserDTO, CartDTO> cartCache;
    private AtomicInteger nextOrderId = new AtomicInteger(1);

    public DefaultOrderService(OrderRepository orderRepository, final OrderDetailService orderDetailService,
                               @Value("${cart.total.to.get.25.percent.discount}") int cartTotalToGet25PercentDiscount,
                               @Value("${items.to.get.cheaper.one.free}") int itemsToGetCheaperOneFree) {
        this.orderRepository = orderRepository;
        this.orderDetailService = orderDetailService;
        this.cartTotalToGet25PercentDiscount = cartTotalToGet25PercentDiscount;
        this.itemsToGetCheaperOneFree = itemsToGetCheaperOneFree;
        cartCache = new ConcurrentHashMap<>();
    }

    @Override
    public CartDTO add(UserDTO user, DrinkDTO drink, Optional<List<ToppingDTO>> toppings) {
        CartDTO cart = getCart(user);

        LOG.info("Creating beverage for user {}- {}.", user.getId(), user.getName());
        Beverage beverage = new Beverage(drink, toppings);
        
        LOG.info("Adding beverage to cart for user {}- {}.", user.getId(), user.getName());
        addBeverageToCart(cart, beverage);
        cartCache.put(user, cart);

        return cart;
    }

    @Override
    public OrderDTO placeOrder(UserDTO user) throws CartNotFoundException {
        checkCartExists(user);

        LOG.info("Retrieving cart for user {}- {} to process the Order.", user.getId(), user.getName());
        CartDTO cart = cartCache.get(user);

        AtomicInteger amountToPay = new AtomicInteger();
        AtomicInteger element = new AtomicInteger(1);

        LOG.info("Calculating order cost for user {}- {}.", user.getId(), user.getName());
        calculateOrderCost(user, cart, amountToPay, element);

        LOG.info("Calculating possible discounts for user {}- {}.", user.getId(), user.getName());
        int discountedAmount = calculateDiscountedAmount(cart, amountToPay);

        LOG.info("Placing order for user {}- {}.", user.getId(), user.getName());
        placeOrder(user, amountToPay, discountedAmount);

        LOG.info("Order processed. Now removing cart for user {}- {}.", user.getId(), user.getName());
        cartCache.remove(user);

        OrderDTO orderDTO = new OrderDTO(amountToPay.get(), discountedAmount, cart);
        return orderDTO;
    }

    @Override
    public List<OrdersPerCustomerDTO> getOrdersPerCustomerList(List<UserDTO> userDTOList) {
        LOG.info("Will get the Orders per Customer.");
        List<OrdersPerCustomerDTO> ordersPerCustomerDTOList = new ArrayList<>();

        for(UserDTO userDTO : userDTOList) {
            List<OrderDTO> orderDTOList = findOrdersByUser(userDTO);

            LOG.info("Will calculate the total amount of orders for User {}- {}", userDTO.getId(), userDTO.getName());
            AtomicInteger totalAmount = new AtomicInteger();
            orderDTOList.forEach(orderDTO -> {
                totalAmount.getAndAdd(orderDTO.getDiscountedAmount());
            });

            OrdersPerCustomerDTO ordersPerCustomerDTO = new OrdersPerCustomerDTO(userDTO.getId(),
                    userDTO.getName(), totalAmount.get());
            ordersPerCustomerDTOList.add(ordersPerCustomerDTO);
        }

        return ordersPerCustomerDTOList;
    }


    private List<OrderDTO> findOrdersByUser(UserDTO user) {
        LOG.info("Retrieving orders for user {}- {}.", user.getId(), user.getName());
        List<Order> orderList = orderRepository.findByUserId(user.getId());
        LOG.info("User {}- {} has {} orders", user.getId(), user.getName(), orderList.size());

        List<OrderDTO> orderDTOList = new ArrayList<>();
        orderList.forEach(order -> {
            orderDTOList.add(orderToOrderDTO(order));
        });

        return orderDTOList;
    }

    private void placeOrder(UserDTO user, AtomicInteger amountToPay, int discountedAmount) {
        Order order = new Order(nextOrderId.getAndIncrement(), user.getId(), amountToPay.get(), discountedAmount);
        orderRepository.save(order);
    }

    private int calculateDiscountedAmount(CartDTO cart, AtomicInteger amount) {
        int discounts = checkDiscounts(cart.getBeverageList(), amount.get());
        return amount.get() - discounts;
    }

    private int checkDiscounts(List<Beverage> beverageList, int amount) {
        int cartAmountDiscount = 0;
        int itemQuantityDiscount = 0;

        if(amount >= cartTotalToGet25PercentDiscount) {
            cartAmountDiscount = getDiscountAmountCartAmountDiscount(amount);
        }

        if(beverageList.size() >= itemsToGetCheaperOneFree) {
            itemQuantityDiscount = getDiscountAmountItemQuantityDiscount(beverageList);
        }

        if (cartAmountDiscount > itemQuantityDiscount) {
            return cartAmountDiscount;
        }

        return itemQuantityDiscount;
    }

    private int getDiscountAmountItemQuantityDiscount(List<Beverage> beverageList) {
        AtomicInteger discount = new AtomicInteger(Integer.MAX_VALUE);

        beverageList.forEach(beverage -> {
            int costOfBeverage = calculateCostOfBeverage(beverage);
            if(costOfBeverage < discount.get()) {
                discount.set(costOfBeverage);
            }
        });

        return discount.get();
    }

    private int calculateCostOfBeverage(Beverage beverage) {
        AtomicInteger cost = new AtomicInteger(beverage.getDrink().getPrice());

        if(beverage.getTopping().isPresent()) {
            beverage.getTopping().get().forEach(topping -> {
                cost.getAndAdd(topping.getPrice());
            });
        }

        return cost.get();
    }

    private int getDiscountAmountCartAmountDiscount(int amount) {
        return (amount * 25) / 100;
    }

    private CartDTO getCart(UserDTO user) {
        LOG.info("Checking if user {} - {} has a cart.", user.getId(), user.getName());
        CartDTO cart;
        if(cartCache.containsKey(user)) {
            LOG.info("User {} - {} has a cart. Retrieving it.", user.getId(), user.getName());
            cart = cartCache.get(user);
        } else {
            LOG.info("User {} - {} does not have a cart. Creating one.", user.getId(), user.getName());
            cart = new CartDTO(user.getId(), user.getName());
        }
        return cart;
    }

    private OrderDTO orderToOrderDTO(Order order) {
        return OrderMapper.INSTANCE.orderToOrderDTO(order);
    }

    private void addBeverageToCart(CartDTO cartDTO, Beverage beverage) {
        cartDTO.getBeverageList().add(beverage);
        updateCartAmount(cartDTO, beverage);
    }

    private void updateCartAmount(CartDTO cartDTO,Beverage beverage) {
        int currentCartAmount = cartDTO.getCartAmount();
        cartDTO.setCartAmount(currentCartAmount + beverage.getDrink().getPrice());
        if(beverage.getTopping().isPresent()) {
            beverage.getTopping().get().forEach(topping ->
                    cartDTO.setCartAmount(currentCartAmount + topping.getPrice())
            );
        }
    }

    private void checkCartExists(UserDTO user) throws CartNotFoundException {
        if(!cartCache.containsKey(user)) {
            throw new CartNotFoundException(user.getId(), user.getName());
        }
    }

    private void calculateOrderCost(UserDTO user, CartDTO cart, AtomicInteger amountToPay, AtomicInteger element) {
        cart.getBeverageList().forEach(beverage -> {
            //For each beverage I only have 1 Drink, so we add it's price to the amount to pay.
            amountToPay.addAndGet(beverage.getDrink().getPrice());
            //Check if the Drink has Toppings
            if(beverage.getTopping().isPresent()) {
                /*The Drink has toppings, so we need to add the cost of each topping to the amount to pay.
                Also, we need to add the combination of Drink + Topping in the Order Detail table.
                 */
                beverage.getTopping().get().forEach(topping -> {
                    amountToPay.addAndGet(topping.getPrice());
                    orderDetailService.saveOrderDetail(nextOrderId.get(), element.getAndIncrement(),
                            beverage.getDrink().getName(), topping.getName(), user.getId());
                });
            } else {
                // The Drink has no toppings. We add the Drink with no topping into the Order Detail table.
                orderDetailService.saveOrderDetail(nextOrderId.get(), element.getAndIncrement(),
                        beverage.getDrink().getName(), EMPTY, user.getId());
            }
        });
    }
}
