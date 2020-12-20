package com.bestseller.starbux.controllers;

import com.bestseller.starbux.domain.Beverage;
import com.bestseller.starbux.domain.dto.BeverageDTO;
import com.bestseller.starbux.domain.dto.CartDTO;
import com.bestseller.starbux.domain.dto.DrinkDTO;
import com.bestseller.starbux.domain.dto.OrderDTO;
import com.bestseller.starbux.domain.dto.ToppingDTO;
import com.bestseller.starbux.exceptions.CartNotFoundException;
import com.bestseller.starbux.exceptions.DrinkNotFoundException;
import com.bestseller.starbux.exceptions.ToppingNotFoundException;
import com.bestseller.starbux.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

@SpringBootTest
public class OrderControllerIntegrationTests {
    @Autowired
    private OrderController orderController;

    public OrderControllerIntegrationTests(){}

    @Test
    public void addTest_Ok_NoTopping() throws UserNotFoundException, ToppingNotFoundException, DrinkNotFoundException {
        BeverageDTO beverageDTO = new BeverageDTO("Latte", Optional.empty());
        DrinkDTO drinkDTO = new DrinkDTO("Latte", 5);
        Beverage beverage = new Beverage(drinkDTO, Optional.empty());

        CartDTO cartDTO = orderController.add(1, beverageDTO);

        assertEquals(cartDTO.getUserId(), 1);
        assertEquals(cartDTO.getUserName(), "Patrick");

        assertFalse(cartDTO.getBeverageList().isEmpty());
        assertEquals(cartDTO.getBeverageList().get(0).getDrink().getName(), beverage.getDrink().getName());
        assertEquals(cartDTO.getBeverageList().get(0).getDrink().getPrice(), beverage.getDrink().getPrice());

        assertFalse(cartDTO.getBeverageList().get(0).getTopping().isPresent());
        assertEquals(cartDTO.getBeverageList().get(0).getTopping(), beverage.getTopping());

        assertEquals(cartDTO.getCartAmount(), 5);
    }

    @Test
    public void addTest_Ok_OneTopping() throws UserNotFoundException, ToppingNotFoundException, DrinkNotFoundException {
        List<String> toppingList = new ArrayList<>();
        toppingList.add("Lemon");

        BeverageDTO beverageDTO = new BeverageDTO("Latte", Optional.of(toppingList));
        DrinkDTO drinkDTO = new DrinkDTO("Latte", 5);
        ToppingDTO toppingDTO = new ToppingDTO("Lemon", 2);
        List<ToppingDTO> toppingDTOList = new ArrayList<>();
        toppingDTOList.add(toppingDTO);
        Beverage beverage = new Beverage(drinkDTO, Optional.of(toppingDTOList));

        CartDTO cartDTO = orderController.add(2, beverageDTO);

        assertEquals(cartDTO.getUserId(), 2);
        assertEquals(cartDTO.getUserName(), "Sara");

        assertFalse(cartDTO.getBeverageList().isEmpty());

        assertEquals(cartDTO.getBeverageList().get(0).getDrink().getName(), beverage.getDrink().getName());
        assertEquals(cartDTO.getBeverageList().get(0).getDrink().getPrice(), beverage.getDrink().getPrice());

        assertTrue(cartDTO.getBeverageList().get(0).getTopping().isPresent());
        assertEquals(cartDTO.getBeverageList().get(0).getTopping().get().get(0).getName(),
                beverage.getTopping().get().get(0).getName());
        assertEquals(cartDTO.getBeverageList().get(0).getTopping().get().get(0).getPrice(),
                beverage.getTopping().get().get(0).getPrice());

        assertEquals(cartDTO.getCartAmount(), 7);
    }

    @Test
    public void addTest_Ok_TwoTopping() throws UserNotFoundException, ToppingNotFoundException, DrinkNotFoundException {
        List<String> toppingList = new ArrayList<>();
        toppingList.add("Lemon");
        toppingList.add("Milk");

        BeverageDTO beverageDTO = new BeverageDTO("Latte", Optional.of(toppingList));
        DrinkDTO drinkDTO = new DrinkDTO("Latte", 5);
        ToppingDTO toppingDTO1 = new ToppingDTO("Lemon", 2);
        ToppingDTO toppingDTO2 = new ToppingDTO("Milk", 2);

        List<ToppingDTO> toppingDTOList = new ArrayList<>();
        toppingDTOList.add(toppingDTO1);
        toppingDTOList.add(toppingDTO2);

        Beverage beverage = new Beverage(drinkDTO, Optional.of(toppingDTOList));

        CartDTO cartDTO = orderController.add(3, beverageDTO);

        assertEquals(cartDTO.getUserId(), 3);
        assertEquals(cartDTO.getUserName(), "Cees");

        assertFalse(cartDTO.getBeverageList().isEmpty());

        assertEquals(cartDTO.getBeverageList().get(0).getDrink().getName(), beverage.getDrink().getName());
        assertEquals(cartDTO.getBeverageList().get(0).getDrink().getPrice(), beverage.getDrink().getPrice());

        assertTrue(cartDTO.getBeverageList().get(0).getTopping().isPresent());
        assertEquals(cartDTO.getBeverageList().get(0).getTopping().get().get(0).getName(),
                beverage.getTopping().get().get(0).getName());
        assertEquals(cartDTO.getBeverageList().get(0).getTopping().get().get(0).getPrice(),
                beverage.getTopping().get().get(0).getPrice());

        assertTrue(cartDTO.getBeverageList().get(0).getTopping().isPresent());
        assertEquals(cartDTO.getBeverageList().get(0).getTopping().get().get(1).getName(),
                beverage.getTopping().get().get(1).getName());
        assertEquals(cartDTO.getBeverageList().get(0).getTopping().get().get(1).getPrice(),
                beverage.getTopping().get().get(0).getPrice());

        assertEquals(cartDTO.getCartAmount(), 9);
    }

    @Test
    public void addTest_Fail_UserNotFound() {
        BeverageDTO beverageDTO = new BeverageDTO("Latte", Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> orderController.add(-1, beverageDTO));
    }

    @Test
    public void addTest_Fail_DrinkNotFound() {
        BeverageDTO beverageDTO = new BeverageDTO("IDontExist", Optional.empty());

        assertThrows(DrinkNotFoundException.class,
                () -> orderController.add(1, beverageDTO));
    }

    @Test
    public void addTest_Fail_ToppingNotFound() {
        List<String> toppingList = new ArrayList<>();
        toppingList.add("IDontExist");

        BeverageDTO beverageDTO = new BeverageDTO("Latte", Optional.of(toppingList));

        assertThrows(ToppingNotFoundException.class,
                () -> orderController.add(1, beverageDTO));
    }

    @Test
    public void placeTest_Ok_SameOriginalAndDiscountedAmount() throws UserNotFoundException,
            ToppingNotFoundException, DrinkNotFoundException, CartNotFoundException {
        BeverageDTO beverageDTO = new BeverageDTO("Latte", Optional.empty());

        CartDTO cartDTO = orderController.add(4, beverageDTO);

        OrderDTO orderDTO = orderController.place(4);

        assertEquals(orderDTO.getDiscountedAmount(), 5);
        assertEquals(orderDTO.getOriginalAmount(), orderDTO.getDiscountedAmount());

        assertEquals(cartDTO.getCartAmount(), orderDTO.getCart().getCartAmount());
        assertEquals(cartDTO.getUserName(), orderDTO.getCart().getUserName());
        assertEquals(cartDTO.getUserId(), orderDTO.getCart().getUserId());
        assertEquals(cartDTO.getBeverageList(), orderDTO.getCart().getBeverageList());
    }

    @Test
    public void placeTest_Ok_LowestPriceDiscount() throws UserNotFoundException,
            ToppingNotFoundException, DrinkNotFoundException, CartNotFoundException {
        BeverageDTO beverageDTO = new BeverageDTO("Latte", Optional.empty());
        BeverageDTO beverageDTO2 = new BeverageDTO("Tea", Optional.empty());

        orderController.add(5, beverageDTO);
        orderController.add(5, beverageDTO2);
        CartDTO cartDTO = orderController.add(5, beverageDTO);


        OrderDTO orderDTO = orderController.place(5);

        assertEquals(orderDTO.getDiscountedAmount(), 10);
        assertEquals(orderDTO.getOriginalAmount(), 13);

        assertEquals(cartDTO.getCartAmount(), orderDTO.getCart().getCartAmount());
        assertEquals(cartDTO.getUserName(), orderDTO.getCart().getUserName());
        assertEquals(cartDTO.getUserId(), orderDTO.getCart().getUserId());
        assertEquals(cartDTO.getBeverageList(), orderDTO.getCart().getBeverageList());
    }

    @Test
    public void placeTest_Ok_25PercentDiscount() throws UserNotFoundException,
            ToppingNotFoundException, DrinkNotFoundException, CartNotFoundException {
        BeverageDTO beverageDTO = new BeverageDTO("Tea", Optional.empty());
        BeverageDTO beverageDTO2 = new BeverageDTO("Mocha", Optional.empty());

        orderController.add(6, beverageDTO2);
        orderController.add(6, beverageDTO2);
        CartDTO cartDTO = orderController.add(6, beverageDTO);


        OrderDTO orderDTO = orderController.place(6);

        assertEquals(orderDTO.getDiscountedAmount(), 12);
        assertEquals(orderDTO.getOriginalAmount(), 15);

        assertEquals(cartDTO.getCartAmount(), orderDTO.getCart().getCartAmount());
        assertEquals(cartDTO.getUserName(), orderDTO.getCart().getUserName());
        assertEquals(cartDTO.getUserId(), orderDTO.getCart().getUserId());
        assertEquals(cartDTO.getBeverageList(), orderDTO.getCart().getBeverageList());
    }

    @Test
    public void placeTest_Fail_CartNotFound() {
        assertThrows(CartNotFoundException.class,
                () -> orderController.place(7));
    }

    @Test
    public void placeTest_Fail_UserNotFound() {
        assertThrows(UserNotFoundException.class,
                () -> orderController.place(-1));
    }
}
