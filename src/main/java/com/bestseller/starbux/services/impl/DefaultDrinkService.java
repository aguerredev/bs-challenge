package com.bestseller.starbux.services.impl;

import com.bestseller.starbux.domain.dto.DrinkDTO;
import com.bestseller.starbux.entities.Drink;
import com.bestseller.starbux.exceptions.DrinkAlreadyExistException;
import com.bestseller.starbux.exceptions.DrinkNotFoundException;
import com.bestseller.starbux.exceptions.NoDrinksFoundException;
import com.bestseller.starbux.mapper.DrinkMapper;
import com.bestseller.starbux.repositories.DrinkRepository;
import com.bestseller.starbux.services.DrinkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultDrinkService implements DrinkService {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultDrinkService.class);

    private final DrinkRepository drinkRepository;

    public DefaultDrinkService(final DrinkRepository drinkRepository) {
        this.drinkRepository = drinkRepository;
    }

    @Override
    public DrinkDTO getByName(String name) throws DrinkNotFoundException {
        Drink drink = getDrink(name);
        return drinkToDrinkDTO(drink);
    }

    @Override
    public DrinkDTO create(DrinkDTO drinkDTO) throws DrinkAlreadyExistException {
        checkDrinkDoesNotExist(drinkDTO.getName());

        LOG.info("Creating drink {} - {} Euro", drinkDTO.getName(), drinkDTO.getPrice());
        Drink createdDrink = drinkRepository.save(drinkDTOToDrink(drinkDTO));
        return drinkToDrinkDTO(createdDrink);
    }

    @Override
    public DrinkDTO update(DrinkDTO drinkDTO) throws DrinkNotFoundException {
        getDrink(drinkDTO.getName());

        LOG.info("Updating drink {} - {} Euro", drinkDTO.getName(), drinkDTO.getPrice());
        Drink createdDrink = drinkRepository.save(drinkDTOToDrink(drinkDTO));
        return drinkToDrinkDTO(createdDrink);
    }

    @Override
    public void delete(String drinkName) throws DrinkNotFoundException {
        Drink drink = getDrink(drinkName);
        DrinkDTO drinkDTO = drinkToDrinkDTO(drink);

        LOG.info("Deleting drink {} - {} Euro", drinkDTO.getName(), drinkDTO.getPrice());
        drinkRepository.delete(drinkDTOToDrink(drinkDTO));
    }

    @Override
    public List<DrinkDTO> find() throws NoDrinksFoundException {
        LOG.info("Getting Drinks from DB");
        Iterable<Drink> listOfDrink = drinkRepository.findAll();
        List<DrinkDTO> listOfDrinkDTO = new ArrayList<>();
        listOfDrink.forEach(drink ->
            listOfDrinkDTO.add(drinkToDrinkDTO(drink))
        );

        if(listOfDrinkDTO.isEmpty()) {
            throw new NoDrinksFoundException();
        }

        return listOfDrinkDTO;
    }

    private Drink getDrink(String name) throws DrinkNotFoundException {
        LOG.info("Checking if drink {} exists", name);

        return drinkRepository.findByName(name)
                .orElseThrow(() -> {
                    DrinkNotFoundException ex = new DrinkNotFoundException(name);
                    LOG.error(ex.getMessage());
                    return ex;
                } );
    }

    private void checkDrinkDoesNotExist(String name) throws DrinkAlreadyExistException {
        LOG.info("Checking if drink {} already exists", name);

        Optional<Drink> drink = drinkRepository.findByName(name);
        if(drink.isPresent()) {
            throw new DrinkAlreadyExistException(name);
        }
    }

    private DrinkDTO drinkToDrinkDTO(Drink drink) {
        return DrinkMapper.INSTANCE.drinkToDrinkDTO(drink);
    }

    private Drink drinkDTOToDrink(DrinkDTO drink) { return DrinkMapper.INSTANCE.drinkDTOToDrink(drink); }
}
