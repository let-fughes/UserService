package com.kirylliuss.shop.service;

import com.kirylliuss.shop.dto.request.UserRequest;
import com.kirylliuss.shop.dto.respons.UserRespons;
import com.kirylliuss.shop.exceptions.UserNotFoundException;
import com.kirylliuss.shop.exceptions.ValidationException;
import com.kirylliuss.shop.mapper.CardMapper;
import com.kirylliuss.shop.mapper.UserMapper;
import com.kirylliuss.shop.model.User;
import com.kirylliuss.shop.repository.UserRepository;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CardMapper cardMapper;

    @Autowired
    public UserService(
            UserRepository userRepository, UserMapper userMapper, CardMapper cardMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.cardMapper = cardMapper;
    }

    @Cacheable(value = "usersList", key = "'allUsers'")
    @Transactional(readOnly = true)
    public List<UserRespons> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toUserResponseWithCards)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "usersById", key = "#id")
    @Transactional(readOnly = true)
    public UserRespons getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return toUserResponseWithCards(user);
    }

    @Cacheable(value = "usersByEmail", key = "#email")
    @Transactional(readOnly = true)
    public UserRespons getUserByEmail(String email) {
        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new UserNotFoundException(email));
        return toUserResponseWithCards(user);
    }

    @Caching(evict = {@CacheEvict(value = "usersList", key = "'allUsers'")})
    @Transactional
    public UserRespons createUser(@Valid UserRequest userRequest) throws ValidationException {
        User user = userMapper.toUser(userRequest);
        User savedUser = userRepository.save(user);
        return toUserResponseWithCards(savedUser);
    }

    @Caching(
            evict = {
                @CacheEvict(value = "usersList", key = "'allUsers'"),
                @CacheEvict(value = "usersById", key = "#id"),
                @CacheEvict(
                        value = "usersByEmail",
                        allEntries = true) // Очищаем кэш по email т.к. email может измениться
            })
    @Transactional
    public UserRespons updateUser(Long id, @Valid UserRequest userRequest)
            throws ValidationException {
        User existingUser =
                userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        userMapper.updateUserFromUserRequest(existingUser, userRequest);
        User updatedUser = userRepository.save(existingUser);

        return toUserResponseWithCards(updatedUser);
    }

    @Caching(
            evict = {
                @CacheEvict(value = "usersList", key = "'allUsers'"),
                @CacheEvict(value = "usersById", key = "#id"),
                @CacheEvict(value = "usersByEmail", allEntries = true)
            })
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }

    private UserRespons toUserResponseWithCards(User user) {
        UserRespons userRespons = userMapper.toUserResponse(user);

        if (user.getCards() != null) {
            userRespons.setCards(
                    user.getCards().stream()
                            .map(cardMapper::toCardResponse)
                            .collect(Collectors.toList()));
        }

        return userRespons;
    }

    // Убрал кэширование для batch операции - оно проблематично
    @Transactional(readOnly = true)
    public List<UserRespons> getUsersByIdIn(List<Long> ids) throws UserNotFoundException {
        List<User> users = userRepository.findByIdIn(ids);
        return users.stream().map(this::toUserResponseWithCards).collect(Collectors.toList());
    }
}
