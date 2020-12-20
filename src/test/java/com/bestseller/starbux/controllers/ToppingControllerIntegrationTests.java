package com.bestseller.starbux.controllers;

import com.bestseller.starbux.controllers.admin.ToppingController;
import com.bestseller.starbux.domain.dto.ToppingDTO;
import com.bestseller.starbux.exceptions.ToppingAlreadyExistException;
import com.bestseller.starbux.exceptions.ToppingNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@SpringBootTest
@Transactional
public class ToppingControllerIntegrationTests {
    @Autowired
    private ToppingController toppingController;

    public ToppingControllerIntegrationTests() {}

    @Test
    public void createTest_Ok() throws ToppingAlreadyExistException {
        ToppingDTO toppingDTO = new ToppingDTO("Water", 1);
        ToppingDTO createdTopping = toppingController.create(toppingDTO);

        assertEquals(createdTopping.getName(), "Water");
        assertEquals(createdTopping.getPrice(), 1);
    }

    @Test
    public void createTest_Fails_ToppingAlreadyExists() {
        ToppingDTO toppingDTO = new ToppingDTO("Milk", 5);

        assertThrows(ToppingAlreadyExistException.class,
                () -> toppingController.create(toppingDTO));
    }

    @Test
    public void updateTest_Ok() throws ToppingNotFoundException {
        ToppingDTO toppingDTO = new ToppingDTO("Milk", 50);
        ToppingDTO createdTopping = toppingController.update(toppingDTO);

        assertEquals(createdTopping.getName(), "Milk");
        assertEquals(createdTopping.getPrice(), 50);
    }

    @Test
    public void updateTest_Fails_ToppingNotFound() {
        ToppingDTO toppingDTO = new ToppingDTO("testTopping", 5);

        assertThrows(ToppingNotFoundException.class,
                () -> toppingController.update(toppingDTO));
    }

    @Test
    public void deleteTest_Ok() throws ToppingNotFoundException {
        toppingController.delete("Lemon");
    }

    @Test
    public void deleteTest_Fails_ToppingNotFound() {
        assertThrows(ToppingNotFoundException.class,
                () -> toppingController.delete("testTopping"));
    }
}
