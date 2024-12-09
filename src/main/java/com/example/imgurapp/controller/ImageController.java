package com.example.imgurapp.controller;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.imgurapp.entity.Image;
import com.example.imgurapp.entity.User;
import com.example.imgurapp.exception.*;
import com.example.imgurapp.service.ImageService;
import com.example.imgurapp.service.UserService;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);
    private final ImageService imageService;
    private final UserService userService;

    @Autowired
    public ImageController(ImageService imageService, UserService userService) {
        this.imageService = imageService;
        this.userService = userService;
    }

    // Endpoint to upload an image
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image,
            @RequestParam String username,
            @RequestParam String password) {
        logger.info("Attempting to upload image for user: {}", username);

        try {
            // Authenticate user before allowing image upload
            User user = userService.authenticateUser(username, password);

            // Call the service method to handle image upload
            Image uploadedImage = imageService.uploadImage(image, user);
            logger.info("Image uploaded successfully: {}", uploadedImage.getLink());
            // Return success response
            return ResponseEntity.ok("Image uploaded successfully: " + uploadedImage.getLink());

        } catch (InvalidCredentialsException ex) {
            logger.error("Invalid credentials for user: {}", username);
            return ResponseEntity.status(401).body("Invalid credentials.");
        } catch (ImageUploadException ex) {
            logger.error("Error during image upload: {}", ex.getMessage());
            return ResponseEntity.status(500).body("Error uploading image.");
        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(404).body("User not found.");
        }
    }

    // Endpoint to get the list of images associated with a user
    @GetMapping("/user/{username}")
    public ResponseEntity<List<Image>> getUserImages(@PathVariable String username,
            @RequestParam String password) {
        try {
            // Authenticate user before fetching images
            User user = userService.authenticateUser(username, password);

            // Call the service to get user images
            List<Image> userImages = imageService.getImagesByUser(user);

            if (userImages.isEmpty()) {
                return ResponseEntity.status(404).body(Collections.emptyList());
            }

            return ResponseEntity.ok(userImages);

        } catch (InvalidCredentialsException ex) {
            return ResponseEntity.status(401).body(null);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(404).body(null);
        }
    }

    // Endpoint to delete an image
    @DeleteMapping("/delete/{deleteHash}")
    public ResponseEntity<String> deleteImage(@PathVariable String deleteHash,
            @RequestParam String username,
            @RequestParam String password) {
        logger.info("Attempting to delete image with ID: {}", deleteHash);
        try {
            // Authenticate user before allowing image deletion
            User user = userService.authenticateUser(username, password);
            imageService.deleteImage(deleteHash, user);
            logger.info("Image deleted successfully with ID: {}", deleteHash);
            return ResponseEntity.ok("Image deleted successfully");
        } catch (ImageNotFoundException e) {
            logger.error("Image not found for deletion with ID: {}", deleteHash);
            return ResponseEntity.status(404).body("Image not found");
        } catch (InvalidCredentialsException e) {
            logger.error("Invalid credentials for user: {}", username);
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
