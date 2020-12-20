package com.bestseller.starbux.services.impl;

import com.bestseller.starbux.domain.dto.UserDTO;
import com.bestseller.starbux.entities.User;
import com.bestseller.starbux.exceptions.NoUsersFoundException;
import com.bestseller.starbux.exceptions.UserNotFoundException;
import com.bestseller.starbux.mapper.UserMapper;
import com.bestseller.starbux.repositories.UserRepository;
import com.bestseller.starbux.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DefaultUserService implements UserService {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultUserService.class);

    private final UserRepository userRepository;

    private Map<Integer, UserDTO> userCache;

    public DefaultUserService(final UserRepository userRepository)
    {
        this.userRepository = userRepository;
        userCache = new ConcurrentHashMap<>();
    }

    @Override
    public UserDTO getById(int id) throws UserNotFoundException {
        LOG.info("Checking if user {} exists", id);
        if(userCache.containsKey(id)) {
            return userCache.get(id);
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    UserNotFoundException ex = new UserNotFoundException(id);
                    LOG.error(ex.getMessage());
                    return ex;
                } );
        return userToUserDTO(user);
    }

    @Override
    public List<UserDTO> find() throws NoUsersFoundException {
        LOG.info("Getting Users from DB");
        Iterable<User> listOfUser = userRepository.findAll();
        List<UserDTO> listOfUserDTO = new ArrayList<>();
        listOfUser.forEach(user ->
            listOfUserDTO.add(userToUserDTO(user))
        );

        if(listOfUserDTO.isEmpty()) {
            throw new NoUsersFoundException();
        }

        return listOfUserDTO;
    }

    @Override
    public boolean removeUserFromCache(int id) {
        if(userCache.containsKey(id)) {
            userCache.remove(id);
            return true;
        }
        return false;
    }

    private UserDTO userToUserDTO(User user) {
        return UserMapper.INSTANCE.userToUserDTO(user);
    }

}
