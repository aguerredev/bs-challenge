package com.bestseller.starbux.repositories;

import com.bestseller.starbux.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Database Access Object for user table.
 */

public interface UserRepository extends CrudRepository<User, Short> {
    Optional<User> findById(int id);
}
