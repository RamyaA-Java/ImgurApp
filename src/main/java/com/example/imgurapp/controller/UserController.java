package com.example.imgurapp.controller;

import com.example.imgurapp.entity.User;
import com.example.imgurapp.exception.InvalidCredentialsException;
import com.example.imgurapp.exception.UserNotFoundException;
import com.example.imgurapp.service.UserService;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Register a new user with basic information (username and password).
     *
     * @param username the username for the new user
     * @param password the password for the new user
     * @return the registered user
     * @throws UserNotFoundException if the username already exists
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestParam String username, @RequestParam String password) {
        logger.info("Attempting to register a new user: {}", username);
        try {
            User registeredUser = userService.registerUser(username, password);
            logger.info("User registered successfully: {}", registeredUser.getUsername());
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            logger.error("Error during registration: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Registration failed: " + e.getMessage());
        }
    }

    /**
     * Authenticate a user by username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return the authenticated user
     * @throws InvalidCredentialsException if credentials are incorrect
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateUser(@RequestParam String username, @RequestParam String password) {
        logger.info("Attempting to authenticate user: {}", username);
        try {
            User authenticatedUser = userService.authenticateUser(username, password);
            logger.info("User authenticated successfully: {}", username);
            return ResponseEntity.ok(authenticatedUser);
        } catch (InvalidCredentialsException e) {
            logger.error("Invalid credentials for user: {}", username);
            return ResponseEntity.status(401).body("Invalid credentials");
        } catch (UserNotFoundException e) {
            logger.error("User not found: {}", username);
            return ResponseEntity.status(404).body("User not found");
        }
    }

    /**
     * Get user details by username.
     *
     * @param username the username of the user
     * @return the user details
     * @throws UserNotFoundException if the user is not found
     */
    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        logger.info("Fetching user info for: {}", username);
        try {
            User user = userService.getUserByUsername(username);
            logger.info("User info fetched successfully: {}", username);
            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
            logger.error("User not found: {}", username);
            return ResponseEntity.status(404).body("User not found");
        }
    }
}
