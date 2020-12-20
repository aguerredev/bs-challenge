package com.bestseller.starbux.services;

import com.bestseller.starbux.domain.dto.DrinkDTO;
import com.bestseller.starbux.domain.dto.MostUsedToppingForDrinksDTO;
import com.bestseller.starbux.entities.OrderDetail;
import com.bestseller.starbux.repositories.OrderDetailRepository;
import com.bestseller.starbux.services.impl.DefaultOrderDetailService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderDetailServiceUnitTests {

    private OrderDetailService orderDetailService;
    private OrderDetailRepository orderDetailRepository;

    public OrderDetailServiceUnitTests() {
        this.orderDetailRepository = mock(OrderDetailRepository.class);
        this.orderDetailService = new DefaultOrderDetailService(orderDetailRepository);
    }

    @Test
    public void saveOrderDetailTest_OK() {
        orderDetailService.saveOrderDetail(1,1,"Tea", "Milk",1);
    }

    @Test
    public void getMostUsedToppingsForDrinkTest_OK() {
        //Preparing DrinkDTOs
        DrinkDTO drinkDTO = mock(DrinkDTO.class);
        List<DrinkDTO> drinkDTOList = new ArrayList<>();
        drinkDTOList.add(drinkDTO);

        when(drinkDTO.getName()).thenReturn("Tea");
        when(drinkDTO.getPrice()).thenReturn(2);

        //Preparing OrderDetails
        OrderDetail orderDetail = mock(OrderDetail.class);
        List<OrderDetail> orderDetailList = new ArrayList<>();
        orderDetailList.add(orderDetail);

        when(orderDetail.getDrink()).thenReturn("Tea");
        when(orderDetail.getTopping()).thenReturn("Milk");
        when(orderDetail.getId()).thenReturn(1);
        when(orderDetail.getElement()).thenReturn(1);
        when(orderDetail.getUserId()).thenReturn(1);

        when(orderDetailRepository.findByDrink("Tea")).thenReturn(orderDetailList);

        List<MostUsedToppingForDrinksDTO> mostUsedToppingForDrinksDTOList =
                orderDetailService.getMostUsedToppingsForDrink(drinkDTOList);

        assertFalse(mostUsedToppingForDrinksDTOList.isEmpty());
        assertEquals(mostUsedToppingForDrinksDTOList.get(0).getDrink(), "Tea");
        assertFalse(mostUsedToppingForDrinksDTOList.get(0).getMostUsedToppings().isEmpty());
        assertEquals(mostUsedToppingForDrinksDTOList.get(0).getMostUsedToppings().get(0), "Milk");
    }
}
