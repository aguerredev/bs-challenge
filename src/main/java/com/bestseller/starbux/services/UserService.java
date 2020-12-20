package com.bestseller.starbux.services;

import com.bestseller.starbux.domain.dto.UserDTO;
import com.bestseller.starbux.exceptions.NoUsersFoundException;
import com.bestseller.starbux.exceptions.UserNotFoundException;

import java.util.List;

public interface UserService {
    UserDTO getById(int id) throws UserNotFoundException;

    boolean removeUserFromCache(int id);

    List<UserDTO> find() throws NoUsersFoundException;
}
