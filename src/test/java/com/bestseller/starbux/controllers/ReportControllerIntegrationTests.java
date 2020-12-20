package com.bestseller.starbux.controllers;

import com.bestseller.starbux.controllers.admin.ReportController;
import com.bestseller.starbux.domain.dto.MostUsedToppingForDrinksDTO;
import com.bestseller.starbux.domain.dto.OrdersPerCustomerDTO;
import com.bestseller.starbux.exceptions.NoDrinksFoundException;
import com.bestseller.starbux.exceptions.NoUsersFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SpringBootTest
public class ReportControllerIntegrationTests {
    @Autowired
    private ReportController reportController;

    public ReportControllerIntegrationTests(){}

    @Test
    public void totalAmountsOfOrdersPerCustomerTest_OK() throws NoUsersFoundException {
        List<OrdersPerCustomerDTO> ordersPerCustomer = reportController.totalAmountsOfOrdersPerCustomer();

        assertFalse(ordersPerCustomer.isEmpty());

        assertEquals(ordersPerCustomer.get(0).getUserName(), "Patrick");
        assertEquals(ordersPerCustomer.get(0).getUserId(), 1);
        assertEquals(ordersPerCustomer.get(0).getTotalAmountOfOrders(), 0);

        assertEquals(ordersPerCustomer.get(1).getUserName(), "Sara");
        assertEquals(ordersPerCustomer.get(1).getUserId(), 2);
        assertEquals(ordersPerCustomer.get(1).getTotalAmountOfOrders(), 0);

        assertEquals(ordersPerCustomer.get(2).getUserName(), "Cees");
        assertEquals(ordersPerCustomer.get(2).getUserId(), 3);
        assertEquals(ordersPerCustomer.get(2).getTotalAmountOfOrders(), 22);
    }

    @Test
    public void mostUsedToppingsForDrinkTest_OK() throws NoDrinksFoundException {
        List<MostUsedToppingForDrinksDTO> mostUsedToppingsForDrink = reportController.mostUsedToppingsForDrink();

        assertFalse(mostUsedToppingsForDrink.isEmpty());

        assertEquals(mostUsedToppingsForDrink.get(0).getDrink(), "Black Coffee");
        assertTrue(mostUsedToppingsForDrink.get(0).getMostUsedToppings().isEmpty());

        assertEquals(mostUsedToppingsForDrink.get(1).getDrink(), "Latte");
        assertFalse(mostUsedToppingsForDrink.get(1).getMostUsedToppings().isEmpty());
        assertEquals(mostUsedToppingsForDrink.get(1).getMostUsedToppings().get(0), "Milk");

        assertEquals(mostUsedToppingsForDrink.get(2).getDrink(), "Mocha");
        assertFalse(mostUsedToppingsForDrink.get(2).getMostUsedToppings().isEmpty());
        assertEquals(mostUsedToppingsForDrink.get(2).getMostUsedToppings().get(0), "Chocolate sauce");

        assertEquals(mostUsedToppingsForDrink.get(3).getDrink(), "Tea");
        assertFalse(mostUsedToppingsForDrink.get(3).getMostUsedToppings().isEmpty());
        assertEquals(mostUsedToppingsForDrink.get(3).getMostUsedToppings().get(0), "Milk");
    }
}
