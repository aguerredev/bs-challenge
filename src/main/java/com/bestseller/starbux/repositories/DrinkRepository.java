package com.bestseller.starbux.repositories;

import com.bestseller.starbux.entities.Drink;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Database Access Object for drink table.
 */

public interface DrinkRepository extends CrudRepository<Drink, Short> {
    Optional<Drink> findByName(String name);
}
