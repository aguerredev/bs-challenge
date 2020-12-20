package com.bestseller.starbux.services.impl;

import com.bestseller.starbux.domain.dto.ToppingDTO;
import com.bestseller.starbux.entities.Topping;
import com.bestseller.starbux.exceptions.ToppingAlreadyExistException;
import com.bestseller.starbux.exceptions.ToppingNotFoundException;
import com.bestseller.starbux.mapper.ToppingMapper;
import com.bestseller.starbux.repositories.ToppingRepository;
import com.bestseller.starbux.services.ToppingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DefaultToppingService implements ToppingService {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultToppingService.class);

    private final ToppingRepository toppingRepository;

    public DefaultToppingService(final ToppingRepository toppingRepository) {
        this.toppingRepository = toppingRepository;
    }

    @Override
    public Optional<List<ToppingDTO>> getByName(Optional<List<String>> toppings) throws ToppingNotFoundException {
        if(!toppings.isPresent()) {
            return Optional.empty();
        }
        List<ToppingDTO> toppingsList = new ArrayList<>();

        for(String topping : toppings.get()) {
            LOG.info("Checking if topping {} exists", topping);
            Topping currentTopping = toppingRepository.findByName(topping)
                    .orElseThrow(() -> {
                        ToppingNotFoundException ex = new ToppingNotFoundException(topping);
                        LOG.error(ex.getMessage());
                        return ex;
                    } );
            toppingsList.add(toppingToToppingDTO(currentTopping));
        }

        return Optional.of(toppingsList);
    }

    @Override
    public ToppingDTO create(ToppingDTO drinkDTO) throws ToppingAlreadyExistException {
        checkToppingDoesNotExist(drinkDTO.getName());

        LOG.info("Creating Topping {} - {} Euro", drinkDTO.getName(), drinkDTO.getPrice());
        Topping createdTopping = toppingRepository.save(toppingDTOToTopping(drinkDTO));
        return toppingToToppingDTO(createdTopping);
    }

    @Override
    public ToppingDTO update(ToppingDTO drinkDTO) throws ToppingNotFoundException {
        getTopping(drinkDTO.getName());

        LOG.info("Updating topping {} - {} Euro", drinkDTO.getName(), drinkDTO.getPrice());
        Topping createdTopping = toppingRepository.save(toppingDTOToTopping(drinkDTO));
        return toppingToToppingDTO(createdTopping);
    }

    @Override
    public void delete(String toppingName) throws ToppingNotFoundException {
        Topping topping = getTopping(toppingName);
        ToppingDTO toppingDTO = toppingToToppingDTO(topping);

        LOG.info("Deleting topping {} - {} Euro", toppingDTO.getName(), toppingDTO.getPrice());
        toppingRepository.delete(toppingDTOToTopping(toppingDTO));
    }

    private Topping getTopping(String name) throws ToppingNotFoundException {
        LOG.info("Checking if topping {} exists", name);

        Optional<Topping> topping = toppingRepository.findByName(name);
        if(!topping.isPresent()) {
            throw new ToppingNotFoundException(name);
        }

        return topping.get();
    }

    private void checkToppingDoesNotExist(String name) throws ToppingAlreadyExistException {
        LOG.info("Checking if topping {} already exists", name);

        Optional<Topping> topping = toppingRepository.findByName(name);
        if(topping.isPresent()) {
            throw new ToppingAlreadyExistException(name);
        }
    }

    private ToppingDTO toppingToToppingDTO(Topping topping) {
        return ToppingMapper.INSTANCE.toppingToToppingDTO(topping);
    }

    private Topping toppingDTOToTopping(ToppingDTO topping) {
        return ToppingMapper.INSTANCE.toppingDTOToTopping(topping);
    }
}
