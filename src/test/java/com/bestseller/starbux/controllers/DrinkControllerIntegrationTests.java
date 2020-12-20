package com.bestseller.starbux.controllers;

import com.bestseller.starbux.controllers.admin.DrinkController;
import com.bestseller.starbux.domain.dto.DrinkDTO;
import com.bestseller.starbux.exceptions.DrinkAlreadyExistException;
import com.bestseller.starbux.exceptions.DrinkNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@SpringBootTest
@Transactional
public class DrinkControllerIntegrationTests {
    @Autowired
    private DrinkController drinkController;

    public DrinkControllerIntegrationTests() {}

    @Test
    public void createTest_Ok() throws DrinkAlreadyExistException {
        DrinkDTO drinkDTO = new DrinkDTO("Espresso", 5);
        DrinkDTO createdDrink = drinkController.create(drinkDTO);

        assertEquals(createdDrink.getName(), "Espresso");
        assertEquals(createdDrink.getPrice(), 5);
    }

    @Test
    public void createTest_Fails_DrinkAlreadyExists() {
        DrinkDTO drinkDTO = new DrinkDTO("Tea", 5);

        assertThrows(DrinkAlreadyExistException.class,
                () -> drinkController.create(drinkDTO));
    }

    @Test
    public void updateTest_Ok() throws DrinkNotFoundException {
        DrinkDTO drinkDTO = new DrinkDTO("Latte", 50);
        DrinkDTO createdDrink = drinkController.update(drinkDTO);

        assertEquals(createdDrink.getName(), "Latte");
        assertEquals(createdDrink.getPrice(), 50);
    }

    @Test
    public void updateTest_Fails_DrinkNotFound() {
        DrinkDTO drinkDTO = new DrinkDTO("TestDrink", 5);

        assertThrows(DrinkNotFoundException.class,
                () -> drinkController.update(drinkDTO));
    }

    @Test
    public void deleteTest_Ok() throws DrinkNotFoundException {
        drinkController.delete("Mocha");
    }

    @Test
    public void deleteTest_Fails_DrinkNotFound() {
        assertThrows(DrinkNotFoundException.class,
                () -> drinkController.delete("TestDrink"));
    }
}
