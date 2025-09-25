package com.kirylliuss.shop.service;

import com.kirylliuss.shop.config.AbstractIntegrationTest;
import com.kirylliuss.shop.dto.request.UserRequest;
import com.kirylliuss.shop.dto.respons.UserRespons;
import com.kirylliuss.shop.exceptions.UserNotFoundException;
import com.kirylliuss.shop.model.User;
import com.kirylliuss.shop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CacheManager cacheManager;

    private UserRequest userRequest;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        clearAllCaches();

        userRequest = new UserRequest();
        userRequest.setName("John Doe");
        userRequest.setEmail("john.doe@example.com");
    }

    @Test
    void createUser_ShouldPersistUserInDatabase() {
        UserRespons result = userService.createUser(userRequest);

        assertNotNull(result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());

        Optional<User> savedUser = userRepository.findById(result.getId());
        assertTrue(savedUser.isPresent());
        assertEquals("John Doe", savedUser.get().getName());
    }

    @Test
    void getUserById_ShouldReturnCorrectUser() {
        UserRespons createdUser = userService.createUser(userRequest);
        Long userId = createdUser.getId();

        UserRespons foundUser = userService.getUserById(userId);

        assertNotNull(foundUser);
        assertEquals(userId, foundUser.getId());
        assertEquals("John Doe", foundUser.getName());
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldThrowException() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(999L));
    }

    @Test
    void getUserByEmail_ShouldReturnCorrectUser() {
        userService.createUser(userRequest);

        UserRespons foundUser = userService.getUserByEmail("john.doe@example.com");

        assertNotNull(foundUser);
        assertEquals("john.doe@example.com", foundUser.getEmail());
        assertEquals("John Doe", foundUser.getName());
    }

    @Test
    @DirtiesContext
    void updateUser_ShouldUpdateUserCorrectly() {
        UserRespons createdUser = userService.createUser(userRequest);
        Long userId = createdUser.getId();

        UserRequest updateRequest = new UserRequest();
        updateRequest.setName("Jane Doe");
        updateRequest.setEmail("jane.doe@example.com");

        UserRespons updatedUser = userService.updateUser(userId, updateRequest);

        assertEquals(userId, updatedUser.getId());
        assertEquals("Jane Doe", updatedUser.getName());
        assertEquals("jane.doe@example.com", updatedUser.getEmail());

        Optional<User> dbUser = userRepository.findById(userId);
        assertTrue(dbUser.isPresent());
        assertEquals("Jane Doe", dbUser.get().getName());
    }

    @Test
    @DirtiesContext
    void deleteUser_ShouldRemoveUserFromDatabase() {
        UserRespons createdUser = userService.createUser(userRequest);
        Long userId = createdUser.getId();

        userService.deleteUser(userId);

        assertFalse(userRepository.existsById(userId));
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        userService.createUser(userRequest);

        UserRequest userRequest2 = new UserRequest();
        userRequest2.setName("Jane Smith");
        userRequest2.setEmail("jane.smith@example.com");
        userService.createUser(userRequest2);

        List<UserRespons> users = userService.getAllUsers();

        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("john.doe@example.com")));
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("jane.smith@example.com")));
    }

    @Test
    void getUsersByIdIn_ShouldReturnUsersWithGivenIds() {
        UserRespons user1 = userService.createUser(userRequest);

        UserRequest userRequest2 = new UserRequest();
        userRequest2.setName("Jane Smith");
        userRequest2.setEmail("jane.smith@example.com");
        UserRespons user2 = userService.createUser(userRequest2);

        List<UserRespons> users = userService.getUsersByIdIn(List.of(user1.getId(), user2.getId()));

        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("john.doe@example.com")));
        assertTrue(users.stream().anyMatch(u -> u.getEmail().equals("jane.smith@example.com")));
    }

    private void clearAllCaches() {
        cacheManager.getCacheNames()
                .forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }
}