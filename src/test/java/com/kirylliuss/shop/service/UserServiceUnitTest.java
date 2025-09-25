package com.kirylliuss.shop.service;

import com.kirylliuss.shop.dto.request.UserRequest;
import com.kirylliuss.shop.dto.respons.UserRespons;
import com.kirylliuss.shop.exceptions.UserNotFoundException;
import com.kirylliuss.shop.mapper.CardMapper;
import com.kirylliuss.shop.mapper.UserMapper;
import com.kirylliuss.shop.model.User;
import com.kirylliuss.shop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserRequest userRequest;
    private UserRespons userRespons;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Test User");

        userRequest = new UserRequest();
        userRequest.setEmail("test@example.com");
        userRequest.setName("Test User");

        userRespons = new UserRespons();
        userRespons.setId(1L);
        userRespons.setEmail("test@example.com");
        userRespons.setName("Test User");
    }

    @Test
    void getAllUsers_ShouldReturnListOfUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toUserResponse(any(User.class))).thenReturn(userRespons);

        List<UserRespons> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test@example.com", result.get(0).getEmail());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponse(any(User.class))).thenReturn(userRespons);

        UserRespons result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_WhenUserNotExists_ShouldThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void createUser_ShouldCreateAndReturnUser() {
        when(userMapper.toUser(any(UserRequest.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserResponse(any(User.class))).thenReturn(userRespons);

        UserRespons result = userService.createUser(userRequest);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void updateUser_WhenUserExists_ShouldUpdateAndReturnUser() {
        UserRequest updateRequest = new UserRequest();
        updateRequest.setEmail("updated@example.com");
        updateRequest.setName("Updated User");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setEmail("updated@example.com");
        updatedUser.setName("Updated User");

        UserRespons updatedResponse = new UserRespons();
        updatedResponse.setId(1L);
        updatedResponse.setEmail("updated@example.com");
        updatedResponse.setName("Updated User");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toUserResponse(any(User.class))).thenReturn(updatedResponse);

        UserRespons result = userService.updateUser(1L, updateRequest);

        assertNotNull(result);
        assertEquals("updated@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUser_WhenUserExists_ShouldDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_WhenUserNotExists_ShouldThrowException() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, never()).deleteById(anyLong());
    }
}