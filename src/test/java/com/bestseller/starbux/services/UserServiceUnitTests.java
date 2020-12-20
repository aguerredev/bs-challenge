package com.bestseller.starbux.services;

import com.bestseller.starbux.domain.dto.UserDTO;
import com.bestseller.starbux.entities.User;
import com.bestseller.starbux.exceptions.UserNotFoundException;
import com.bestseller.starbux.repositories.UserRepository;
import com.bestseller.starbux.services.impl.DefaultUserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

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
    public void removeUserFromCacheTest_UserNotInCache() {
        boolean result = userService.removeUserFromCache(0);
        assertFalse(result);
    }

}
