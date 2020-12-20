package com.bestseller.starbux.services;

import com.bestseller.starbux.domain.dto.CartDTO;
import com.bestseller.starbux.domain.dto.DrinkDTO;
import com.bestseller.starbux.domain.dto.OrderDTO;
import com.bestseller.starbux.domain.dto.OrdersPerCustomerDTO;
import com.bestseller.starbux.domain.dto.ToppingDTO;
import com.bestseller.starbux.domain.dto.UserDTO;
import com.bestseller.starbux.exceptions.CartNotFoundException;
import com.bestseller.starbux.repositories.OrderRepository;
import com.bestseller.starbux.services.impl.DefaultOrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceUnitTests {
    private OrderService orderService;

    private OrderRepository orderRepository;
    private OrderDetailService orderDetailService;

    @Mock
    DrinkDTO drink;

    @Mock
    ToppingDTO topping;

    @Mock
    UserDTO user;

    public OrderServiceUnitTests() {
        this.orderRepository = mock(OrderRepository.class);
        this.orderDetailService = mock(OrderDetailService.class);
        this.orderService = new DefaultOrderService(orderRepository, orderDetailService, 12, 3);
    }

    @Test
    public void addTest_Ok_NoToppings() {
        when(user.getId()).thenReturn(2);
        when(user.getName()).thenReturn("Sara");

        when(drink.getName()).thenReturn("Latte");
        when(drink.getPrice()).thenReturn(5);

        CartDTO testCart = orderService.add(user, drink, Optional.empty());

        assertEquals(testCart.getUserName(), "Sara");
        assertEquals(testCart.getUserId(), 2);
        assertFalse(testCart.getBeverageList().isEmpty());
        assertEquals(testCart.getBeverageList().size(), 1);
        assertEquals(testCart.getBeverageList().get(0).getDrink(), drink);
        assertFalse(testCart.getBeverageList().get(0).getTopping().isPresent());
    }

    @Test
    public void addTest_Ok_WithTopping() {
        when(user.getId()).thenReturn(2);
        when(user.getName()).thenReturn("Sara");

        when(drink.getName()).thenReturn("Latte");
        when(drink.getPrice()).thenReturn(5);

        when(topping.getName()).thenReturn("Lemon");
        when(topping.getPrice()).thenReturn(2);

        List<ToppingDTO> toppingList = new ArrayList<>();
        toppingList.add(topping);

        CartDTO testCart = orderService.add(user, drink, Optional.of(toppingList));

        assertEquals(testCart.getUserName(), "Sara");
        assertEquals(testCart.getUserId(), 2);
        assertFalse(testCart.getBeverageList().isEmpty());
        assertEquals(testCart.getBeverageList().size(), 1);
        assertEquals(testCart.getBeverageList().get(0).getDrink(), drink);
        assertTrue(testCart.getBeverageList().get(0).getTopping().isPresent());
        assertFalse(testCart.getBeverageList().get(0).getTopping().get().isEmpty());
        assertEquals(testCart.getBeverageList().get(0).getTopping().get().get(0), topping);
    }


    @Test
    public void placeOrderTest_Ok_NoDiscounts() throws CartNotFoundException {
        when(user.getId()).thenReturn(2);
        when(user.getName()).thenReturn("Sara");

        when(drink.getName()).thenReturn("Latte");
        when(drink.getPrice()).thenReturn(5);

        orderService.add(user, drink, Optional.empty());

        OrderDTO orderDTO = orderService.placeOrder(user);

        assertEquals(orderDTO.getDiscountedAmount(), orderDTO.getOriginalAmount());
        assertEquals(orderDTO.getDiscountedAmount(), 5);
        assertEquals(orderDTO.getOriginalAmount(), 5);
        assertEquals(orderDTO.getCart().getUserName(), "Sara");
        assertEquals(orderDTO.getCart().getUserId(), 2);
        assertFalse(orderDTO.getCart().getBeverageList().isEmpty());
    }

    @Test
    public void placeOrderTest_Ok_ItemQuantityDiscounts() throws CartNotFoundException {
        when(user.getId()).thenReturn(2);
        when(user.getName()).thenReturn("Sara");

        when(drink.getName()).thenReturn("Latte");
        when(drink.getPrice()).thenReturn(5);

        when(topping.getName()).thenReturn("Chocolate sauce");
        when(topping.getPrice()).thenReturn(5);

        List<ToppingDTO> toppingDTOList = new ArrayList<>();
        toppingDTOList.add(topping);

        orderService.add(user, drink, Optional.empty());

        orderService.add(user, drink, Optional.empty());

        orderService.add(user, drink, Optional.of(toppingDTOList));

        OrderDTO orderDTO = orderService.placeOrder(user);

        assertNotEquals(orderDTO.getDiscountedAmount(), orderDTO.getOriginalAmount());
        assertEquals(orderDTO.getDiscountedAmount(), 15);
        assertEquals(orderDTO.getOriginalAmount(), 20);
        assertEquals(orderDTO.getCart().getUserName(), "Sara");
        assertEquals(orderDTO.getCart().getUserId(), 2);
        assertFalse(orderDTO.getCart().getBeverageList().isEmpty());
    }

    @Test
    public void placeOrderTest_Ok_25PercentDiscounts() throws CartNotFoundException {
        when(user.getId()).thenReturn(2);
        when(user.getName()).thenReturn("Sara");

        when(drink.getName()).thenReturn("Latte");
        when(drink.getPrice()).thenReturn(5);

        when(topping.getName()).thenReturn("Chocolate sauce");
        when(topping.getPrice()).thenReturn(5);

        List<ToppingDTO> toppingDTOList = new ArrayList<>();
        toppingDTOList.add(topping);

        orderService.add(user, drink, Optional.of(toppingDTOList));

        orderService.add(user, drink, Optional.of(toppingDTOList));

        OrderDTO orderDTO = orderService.placeOrder(user);

        assertNotEquals(orderDTO.getDiscountedAmount(), orderDTO.getOriginalAmount());
        assertEquals(orderDTO.getDiscountedAmount(), 15);
        assertEquals(orderDTO.getOriginalAmount(), 20);
        assertEquals(orderDTO.getCart().getUserName(), "Sara");
        assertEquals(orderDTO.getCart().getUserId(), 2);
        assertFalse(orderDTO.getCart().getBeverageList().isEmpty());
    }

    @Test
    public void placeOrderTest_CartNotFound(){
        assertThrows(CartNotFoundException.class,
                () -> orderService.placeOrder(user));
    }


    @Test
    public void getOrdersPerCustomerList_Ok() {
        when(user.getId()).thenReturn(-1);
        when(user.getName()).thenReturn("Ghost");

        List<UserDTO> userDTOList = new ArrayList<>();
        userDTOList.add(user);

        List<OrdersPerCustomerDTO> ordersPerCustomerDTOList = orderService.getOrdersPerCustomerList(userDTOList);

        assertFalse(ordersPerCustomerDTOList.isEmpty());
    }
}
