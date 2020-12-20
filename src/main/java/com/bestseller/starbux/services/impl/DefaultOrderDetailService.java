package com.bestseller.starbux.services.impl;

import com.bestseller.starbux.domain.dto.DrinkDTO;
import com.bestseller.starbux.domain.dto.MostUsedToppingForDrinksDTO;
import com.bestseller.starbux.domain.dto.OrderDetailDTO;
import com.bestseller.starbux.entities.OrderDetail;
import com.bestseller.starbux.mapper.OrderDetailMapper;
import com.bestseller.starbux.repositories.OrderDetailRepository;
import com.bestseller.starbux.services.OrderDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DefaultOrderDetailService implements OrderDetailService {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultOrderDetailService.class);
    private final String EMPTY = "";

    private final OrderDetailRepository orderDetailRepository;

    public DefaultOrderDetailService(final OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public void saveOrderDetail(int orderId, int element, String drink, String topping, int userId) {
        OrderDetail orderDetail = new OrderDetail(orderId, element, drink, topping, userId);
        orderDetailRepository.save(orderDetail);
    }

    @Override
    public List<MostUsedToppingForDrinksDTO> getMostUsedToppingsForDrink(List<DrinkDTO> drinkDTOList) {
        LOG.info("Will get the Order Details per Drink.");
        List<MostUsedToppingForDrinksDTO> mostUsedToppingForDrinksDTOList = new ArrayList<>();
        for(DrinkDTO drinkDTO : drinkDTOList) {
            List<OrderDetailDTO> orderDetailDTOList = findToppingsByDrink(drinkDTO);
            LOG.info("Will calculate the most used toppings for Drink {}", drinkDTO.getName());
            Map<String, Integer> toppingsMap = new HashMap<>();
            checkTimesToppingsWereUsed(orderDetailDTOList, toppingsMap);

            List<String> mostUsedToppings = new ArrayList<>();
            calculateMostUsedToppings(toppingsMap, mostUsedToppings);

            mostUsedToppingForDrinksDTOList.add(new MostUsedToppingForDrinksDTO(drinkDTO.getName(), mostUsedToppings));
        }
        return mostUsedToppingForDrinksDTOList;
    }

    private void calculateMostUsedToppings(Map<String, Integer> toppingsMap, List<String> mostUsedToppings) {
        int timesMostUsedToppings = 0;
        for(Map.Entry<String, Integer> entry : toppingsMap.entrySet()) {
            if(mostUsedToppings.isEmpty()) {
                timesMostUsedToppings = entry.getValue();
                mostUsedToppings.add(entry.getKey());
            } else {
                if(entry.getValue() > timesMostUsedToppings) {
                    mostUsedToppings.clear();
                    timesMostUsedToppings = entry.getValue();
                    mostUsedToppings.add(entry.getKey());
                } else if (entry.getValue() == timesMostUsedToppings) {
                    mostUsedToppings.add(entry.getKey());
                }
            }
        }
    }

    private void checkTimesToppingsWereUsed(List<OrderDetailDTO> orderDetailDTOList, Map<String, Integer> toppingsMap) {
        orderDetailDTOList.forEach(orderDetailDTO -> {
            if(!orderDetailDTO.getTopping().equals(EMPTY)){
                if(toppingsMap.containsKey(orderDetailDTO.getTopping())) {
                    toppingsMap.put(orderDetailDTO.getTopping(), toppingsMap.get(orderDetailDTO.getTopping()) + 1);
                } else {
                    toppingsMap.put(orderDetailDTO.getTopping(), 1);
                }
            }
        });
    }

    private List<OrderDetailDTO> findToppingsByDrink(DrinkDTO drinkDTO) {
        LOG.info("Retrieving Toppings for Drink {}", drinkDTO.getName());
        List<OrderDetail> orderDetails = orderDetailRepository.findByDrink(drinkDTO.getName());
        LOG.info("Drink {} has {} toppings", drinkDTO.getName(), orderDetails.size());

        List<OrderDetailDTO> orderDetailDTOList = new ArrayList<>();
        orderDetails.forEach(orderDetail -> orderDetailDTOList.add(orderDetailToOrderDetailDTO(orderDetail)));

        return orderDetailDTOList;
    }

    private OrderDetailDTO orderDetailToOrderDetailDTO(OrderDetail orderDetail) {
        return OrderDetailMapper.INSTANCE.orderDetailToOrderDetailDTO(orderDetail);
    }
}
