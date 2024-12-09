package com.example.imgurapp.service;

import com.example.imgurapp.entity.User;
import com.example.imgurapp.exception.InvalidCredentialsException;
import com.example.imgurapp.exception.UserNotFoundException;
import com.example.imgurapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Auto-wire PasswordEncoder

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a new user with a username and password.
     *
     * @param username the username for the new user
     * @param password the raw password for the new user
     * @return the saved User object
     * @throws UserNotFoundException if the username is already taken
     */
    public User registerUser(String username, String password) {
        // Check if the username already exists
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserNotFoundException("Username already exists.");
        }

        // Encode the raw password
        String encodedPassword = passwordEncoder.encode(password);

        // Create and save the new user
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    /**
     * Authenticate a user by username and password.
     *
     * @param username    the username of the user
     * @param rawPassword the raw password of the user
     * @return the authenticated User object
     * @throws InvalidCredentialsException if authentication fails
     */
    public User authenticateUser(String username, String rawPassword) {
        // Fetch the user by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid username or password."));

        // Check if the raw password matches the encoded password
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password.");
        }

        return user;
    }

    /**
     * Get user information by username.
     *
     * @param username the username of the user
     * @return the User object if found
     * @throws UserNotFoundException if the user is not found
     */
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found."));
    }
}
