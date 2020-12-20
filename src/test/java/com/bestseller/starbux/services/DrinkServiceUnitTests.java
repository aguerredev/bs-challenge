package com.bestseller.starbux.services;

import com.bestseller.starbux.domain.dto.DrinkDTO;
import com.bestseller.starbux.entities.Drink;
import com.bestseller.starbux.exceptions.DrinkAlreadyExistException;
import com.bestseller.starbux.exceptions.DrinkNotFoundException;
import com.bestseller.starbux.exceptions.NoDrinksFoundException;
import com.bestseller.starbux.repositories.DrinkRepository;
import com.bestseller.starbux.services.impl.DefaultDrinkService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class DrinkServiceUnitTests {

    private DrinkService drinkService;

    private DrinkRepository drinkRepository;

    @Mock
    private DrinkDTO mockedDrinkDTO;

    @Mock
    private Drink mockedDrink;

    public DrinkServiceUnitTests() {
        this.drinkRepository = mock(DrinkRepository.class);
        this.drinkService = new DefaultDrinkService(drinkRepository);
    }

    @Test
    public void getByNameTest_Ok() throws DrinkNotFoundException {
        when(drinkRepository.findByName("Latte")).thenReturn(Optional.of(mockedDrink));
        when(mockedDrink.getName()).thenReturn("Latte");
        when(mockedDrink.getPrice()).thenReturn(5);

        DrinkDTO testDrink = drinkService.getByName("Latte");

        assertEquals(testDrink.getName(), "Latte");
        assertEquals(testDrink.getPrice(), 5);
    }

    @Test
    public void getByNameTest_NotFound() {
        when(drinkRepository.findByName("Latte")).thenReturn(Optional.empty());

        assertThrows(DrinkNotFoundException.class,
                () -> drinkService.getByName("Latte"));
    }

    @Test
    public void createTest_Ok() throws DrinkAlreadyExistException {
        when(mockedDrinkDTO.getName()).thenReturn("TestDrink");
        when(mockedDrinkDTO.getPrice()).thenReturn(1);

        when(mockedDrink.getName()).thenReturn("TestDrink");
        when(mockedDrink.getPrice()).thenReturn(1);

        when(drinkRepository.save(any())).thenReturn(mockedDrink);

        DrinkDTO drinkDTO = drinkService.create(mockedDrinkDTO);
        assertEquals(drinkDTO.getName(), mockedDrinkDTO.getName());
        assertEquals(drinkDTO.getPrice(), mockedDrinkDTO.getPrice());
    }

    @Test
    public void createTest_Fail_DrinkAlreadyExists() {
        when(mockedDrinkDTO.getName()).thenReturn("Latte");
        when(mockedDrinkDTO.getPrice()).thenReturn(5);

        when(drinkRepository.findByName("Latte")).thenReturn(Optional.of(mockedDrink));

        assertThrows(DrinkAlreadyExistException.class,
                () -> drinkService.create(mockedDrinkDTO));
    }

    @Test
    public void updateTest_Ok() throws DrinkNotFoundException {
        when(mockedDrinkDTO.getName()).thenReturn("Tea");
        when(mockedDrinkDTO.getPrice()).thenReturn(10);

        when(mockedDrink.getName()).thenReturn("Tea");
        when(mockedDrink.getPrice()).thenReturn(10);

        when(drinkRepository.findByName("Tea")).thenReturn(Optional.of(mockedDrink));
        when(drinkRepository.save(any())).thenReturn(mockedDrink);

        DrinkDTO drinkDTO = drinkService.update(mockedDrinkDTO);
        assertEquals(drinkDTO.getName(), mockedDrinkDTO.getName());
        assertEquals(drinkDTO.getPrice(), mockedDrinkDTO.getPrice());
    }

    @Test
    public void updateTest_Fail_DrinkDoesNotExist() {
        when(mockedDrinkDTO.getName()).thenReturn("AnotherTestDrink");
        when(mockedDrinkDTO.getPrice()).thenReturn(5);

        assertThrows(DrinkNotFoundException.class,
                () -> drinkService.update(mockedDrinkDTO));
    }

    @Test
    public void deleteTest_Ok() throws DrinkNotFoundException {
        when(mockedDrinkDTO.getName()).thenReturn("Latte");
        when(mockedDrinkDTO.getPrice()).thenReturn(5);

        when(drinkRepository.findByName("Latte")).thenReturn(Optional.of(mockedDrink));

        drinkService.delete("Latte");
    }

    @Test
    public void deleteTest_Fail_DrinkDoesNotExist() {
        assertThrows(DrinkNotFoundException.class,
                () -> drinkService.delete("Latte"));
    }

    @Test
    public void findTest_Ok() throws NoDrinksFoundException {
        List<Drink> mockedDrinks = new ArrayList<>();
        mockedDrinks.add(mockedDrink);

        when(drinkRepository.findAll()).thenReturn(mockedDrinks);

        List<DrinkDTO> drinkDTOList = drinkService.find();

        assertFalse(drinkDTOList.isEmpty());
    }

    @Test
    public void findTest_Fail_NoDrinksFound() {
        assertThrows(NoDrinksFoundException.class,
                () -> drinkService.find());
    }

}
