package com.kirylliuss.shop.controller;

import com.kirylliuss.shop.dto.request.UserRequest;
import com.kirylliuss.shop.dto.respons.UserRespons;
import com.kirylliuss.shop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management API")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/home")
    @Operation(summary = "Welcome page")
    public String home() {
        return "Users API";
    }

    @GetMapping("/email/{email}")
    @Operation(
            summary = "Get user by email",
            responses = {
                @ApiResponse(responseCode = "200", description = "Successfully got user"),
                @ApiResponse(responseCode = "404", description = "User not found"),
                @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<UserRespons> getUserByEmail(@PathVariable String email) {
        UserRespons user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/batch")
    @Operation(
            summary = "Get users by list of IDs",
            responses = {
                @ApiResponse(responseCode = "200", description = "Successfully got users"),
                @ApiResponse(responseCode = "404", description = "Users not found"),
                @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<List<UserRespons>> getUsersByIdIn(@RequestParam List<Long> ids) {
        List<UserRespons> users = userService.getUsersByIdIn(ids);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all users",
            responses = {
                @ApiResponse(responseCode = "200", description = "Successfully retrieved users"),
                @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<List<UserRespons>> getAllUsers() {
        List<UserRespons> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/id/{id}")
    @Operation(
            summary = "Get user by id",
            responses = {
                @ApiResponse(responseCode = "200", description = "Successfully got user"),
                @ApiResponse(responseCode = "404", description = "User not found"),
                @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<UserRespons> getUserById(@PathVariable Long id) {
        UserRespons user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    @Operation(
            summary = "Create new user",
            responses = {
                @ApiResponse(responseCode = "201", description = "User created successfully"),
                @ApiResponse(responseCode = "400", description = "Invalid input data"),
                @ApiResponse(responseCode = "409", description = "Email already exists"),
                @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<UserRespons> createUser(@Valid @RequestBody UserRequest userRequest) {
        UserRespons createdUser = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/update/id/{id}")
    @Operation(
            summary = "Update user",
            responses = {
                @ApiResponse(responseCode = "200", description = "User updated successfully"),
                @ApiResponse(responseCode = "400", description = "Invalid input data"),
                @ApiResponse(responseCode = "404", description = "User not found"),
                @ApiResponse(responseCode = "409", description = "Email already exists"),
                @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<UserRespons> updateUser(
            @PathVariable Long id, @Valid @RequestBody UserRequest userRequest) {
        UserRespons updatedUser = userService.updateUser(id, userRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/id/{id}")
    @Operation(
            summary = "Delete user",
            responses = {
                @ApiResponse(responseCode = "204", description = "User deleted successfully"),
                @ApiResponse(responseCode = "404", description = "User not found"),
                @ApiResponse(responseCode = "500", description = "Internal server error")
            })
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
