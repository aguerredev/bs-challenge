package com.bestseller.starbux.services;

import com.bestseller.starbux.domain.dto.ToppingDTO;
import com.bestseller.starbux.entities.Topping;
import com.bestseller.starbux.exceptions.ToppingAlreadyExistException;
import com.bestseller.starbux.exceptions.ToppingNotFoundException;
import com.bestseller.starbux.repositories.ToppingRepository;
import com.bestseller.starbux.services.impl.DefaultToppingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ToppingServiceUnitTests {

    private ToppingService toppingService;

    private ToppingRepository toppingRepository;

    @Mock
    private ToppingDTO mockedToppingDTO;

    @Mock
    private Topping mockedTopping;

    public ToppingServiceUnitTests() {
        this.toppingRepository = mock(ToppingRepository.class);
        this.toppingService = new DefaultToppingService(toppingRepository);
    }

    @Test
    public void getByNameTest_Ok_NoTopping() throws ToppingNotFoundException {
        Optional<List<ToppingDTO>> testToppings = toppingService.getByName(Optional.empty());
        assertFalse(testToppings.isPresent());
    }

    @Test
    public void getByNameTest_Ok_1Topping() throws ToppingNotFoundException {
        List<String> toppingNames = new ArrayList<>();
        toppingNames.add("Milk");

        when(mockedTopping.getName()).thenReturn("Milk");
        when(mockedTopping.getPrice()).thenReturn(2);

        when(toppingRepository.findByName("Milk")).thenReturn(Optional.of(mockedTopping));

        Optional<List<ToppingDTO>> testToppings = toppingService.getByName(Optional.of(toppingNames));

        assertTrue(testToppings.isPresent());
        assertEquals(testToppings.get().get(0).getName(), "Milk");
        assertEquals(testToppings.get().get(0).getPrice(), 2);
    }

    @Test
    public void getByNameTest_Ok_2Toppings() throws ToppingNotFoundException {
        List<String> toppingNames = new ArrayList<>();
        toppingNames.add("Hazelnut syrup");
        toppingNames.add("Milk");

        Topping anotherMockedTopping = mock(Topping.class);

        when(mockedTopping.getName()).thenReturn("Milk");
        when(mockedTopping.getPrice()).thenReturn(2);

        when(anotherMockedTopping.getName()).thenReturn("Hazelnut syrup");
        when(anotherMockedTopping.getPrice()).thenReturn(3);

        when(toppingRepository.findByName("Milk")).thenReturn(Optional.of(mockedTopping));
        when(toppingRepository.findByName("Hazelnut syrup")).thenReturn(Optional.of(anotherMockedTopping));

        Optional<List<ToppingDTO>> testToppings = toppingService.getByName(Optional.of(toppingNames));

        assertTrue(testToppings.isPresent());

        assertEquals(testToppings.get().get(0).getName(), "Hazelnut syrup");
        assertEquals(testToppings.get().get(0).getPrice(), 3);

        assertEquals(testToppings.get().get(1).getName(), "Milk");
        assertEquals(testToppings.get().get(1).getPrice(), 2);
    }

    @Test
    public void getByNameTest_NotFound_1Topping() {
        List<String> toppingNames = new ArrayList<>();
        toppingNames.add("NotFound");

        assertThrows(ToppingNotFoundException.class,
                () -> toppingService.getByName(Optional.of(toppingNames)));
    }

    @Test
    public void getByNameTest_NotFound_1ToppingOk_1ToppingNotFound() {
        List<String> toppingNames = new ArrayList<>();
        toppingNames.add("Lemon");
        toppingNames.add("NotFound");

        assertThrows(ToppingNotFoundException.class,
                () -> toppingService.getByName(Optional.of(toppingNames)));
    }

    @Test
    public void createTest_Ok() throws ToppingAlreadyExistException {
        when(mockedToppingDTO.getName()).thenReturn("TestTopping");
        when(mockedToppingDTO.getPrice()).thenReturn(1);

        when(mockedTopping.getName()).thenReturn("TestTopping");
        when(mockedTopping.getPrice()).thenReturn(1);

        when(toppingRepository.save(any())).thenReturn(mockedTopping);

        ToppingDTO toppingDTO = toppingService.create(mockedToppingDTO);
        assertEquals(toppingDTO.getName(), mockedToppingDTO.getName());
        assertEquals(toppingDTO.getPrice(), mockedToppingDTO.getPrice());
    }

    @Test
    public void createTest_Fail_DrinkAlreadyExists() {
        when(mockedToppingDTO.getName()).thenReturn("Lemon");
        when(mockedToppingDTO.getPrice()).thenReturn(2);

        when(toppingRepository.findByName("Lemon")).thenReturn(Optional.of(mockedTopping));

        assertThrows(ToppingAlreadyExistException.class,
                () -> toppingService.create(mockedToppingDTO));
    }

    @Test
    public void updateTest_Ok() throws ToppingNotFoundException {
        when(mockedToppingDTO.getName()).thenReturn("Lemon");
        when(mockedToppingDTO.getPrice()).thenReturn(10);

        when(mockedTopping.getName()).thenReturn("Lemon");
        when(mockedTopping.getPrice()).thenReturn(10);

        when(toppingRepository.findByName("Lemon")).thenReturn(Optional.of(mockedTopping));
        when(toppingRepository.save(any())).thenReturn(mockedTopping);

        ToppingDTO toppingDTO = toppingService.update(mockedToppingDTO);
        assertEquals(toppingDTO.getName(), mockedToppingDTO.getName());
        assertEquals(toppingDTO.getPrice(), mockedToppingDTO.getPrice());
    }

    @Test
    public void updateTest_Fail_DrinkDoesNotExist() {
        when(mockedToppingDTO.getName()).thenReturn("TestTopping");
        when(mockedToppingDTO.getPrice()).thenReturn(5);

        assertThrows(ToppingNotFoundException.class,
                () -> toppingService.update(mockedToppingDTO));
    }

    @Test
    public void deleteTest_Ok() throws ToppingNotFoundException {
        when(toppingRepository.findByName("Lemon")).thenReturn(Optional.of(mockedTopping));

        toppingService.delete("Lemon");
    }

    @Test
    public void deleteTest_Fail_DrinkDoesNotExist() {
        assertThrows(ToppingNotFoundException.class,
                () -> toppingService.delete("TestTopping"));
    }
}
