package com.bestseller.starbux.services;

import com.bestseller.starbux.domain.dto.UserDTO;
import com.bestseller.starbux.entities.User;
import com.bestseller.starbux.exceptions.NoUsersFoundException;
import com.bestseller.starbux.exceptions.UserNotFoundException;
import com.bestseller.starbux.repositories.UserRepository;
import com.bestseller.starbux.services.impl.DefaultUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceUnitTests {

    private UserService userService;
    private UserRepository userRepository;

    @Mock
    private User mockedUser;

    public UserServiceUnitTests() {
        this.userRepository = mock(UserRepository.class);
        this.userService = new DefaultUserService(userRepository);
    }

    @Test
    public void getByNameTest_Ok() throws UserNotFoundException {
        when(mockedUser.getId()).thenReturn(1);
        when(mockedUser.getName()).thenReturn("Patrick");

        when(userRepository.findById(1)).thenReturn(Optional.of(mockedUser));

        UserDTO testUser = userService.getById(1);

        assertEquals(testUser.getId(), 1);
        assertEquals(testUser.getName(), "Patrick");
    }

    @Test
    public void getByNameTest_NotFound() {
        assertThrows(UserNotFoundException.class,
                () -> userService.getById(0));
    }

    @Test
    public void findTest_Ok() throws NoUsersFoundException {
        when(mockedUser.getId()).thenReturn(1);
        when(mockedUser.getName()).thenReturn("Patrick");

        List<User> users = new ArrayList<>();
        users.add(mockedUser);

        when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> userDTOList = userService.find();

        assertFalse(userDTOList.isEmpty());
        assertEquals(userDTOList.get(0).getId(), 1);
        assertEquals(userDTOList.get(0).getName(), "Patrick");
    }

    @Test
    public void findTest_Fail_NoUsersFound() {
        assertThrows(NoUsersFoundException.class,
                () -> userService.find());
    }

    @Test
    public void removeUserFromCacheTest_UserNotInCache() {
        boolean result = userService.removeUserFromCache(0);
        assertFalse(result);
    }
}
