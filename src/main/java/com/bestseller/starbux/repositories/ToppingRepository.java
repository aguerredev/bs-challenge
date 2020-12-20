package com.bestseller.starbux.repositories;

import com.bestseller.starbux.entities.Topping;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Database Access Object for topping table.
 */

public interface ToppingRepository extends CrudRepository<Topping, Short> {
    Optional<Topping> findByName(String name);
}
