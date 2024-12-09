package com.example.imgurapp.service;

import com.example.imgurapp.entity.Image;
import com.example.imgurapp.entity.User;
import com.example.imgurapp.repository.ImageRepository;
import com.example.imgurapp.config.ImgurConfig;
import com.example.imgurapp.dto.ImgurUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final RestTemplate restTemplate;
    private final ImgurConfig imgurConfig;

    private static final String IMGUR_API_URL = "https://api.imgur.com/3/image";

    @Autowired
    public ImageService(ImageRepository imageRepository, RestTemplate restTemplate, ImgurConfig imgurConfig) {
        this.imageRepository = imageRepository;
        this.restTemplate = restTemplate;
        this.imgurConfig = imgurConfig;
    }

    /**
     * Upload an image to Imgur and associate it with a user.
     *
     * @param file the image file to upload
     * @param user the user uploading the image
     * @return the saved Image object
     * @throws RuntimeException if there is an issue during upload
     */
    public Image uploadImage(MultipartFile image, User user) {
        // Set the Client-ID in the Authorization header
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Client-ID " + imgurConfig.getImgurClientId());

        try {
            byte[] imageBytes = image.getBytes(); // Convert the MultipartFile to a byte array

            // Prepare the request entity (send the image bytes along with headers)
            HttpEntity<byte[]> requestEntity = new HttpEntity<>(imageBytes, headers);

            // Send the POST request to Imgur API and capture the response
            ResponseEntity<ImgurUploadResponse> responseEntity = restTemplate.exchange(
                    IMGUR_API_URL, HttpMethod.POST, requestEntity, ImgurUploadResponse.class);

            // Extract the response body
            ImgurUploadResponse uploadResponse = responseEntity.getBody();

            // Check if the upload was successful
            if (uploadResponse != null && uploadResponse.isSuccess()) {
                // Extract relevant data from the response
                String imgurId = uploadResponse.getData().getId(); // Get the image ID
                String link = uploadResponse.getData().getLink(); // Get the image link
                String deleteHash = uploadResponse.getData().getDeletehash(); // Get the delete hash

                // Create an Image object and set the properties
                Image img = new Image();
                img.setImgurId(imgurId);
                img.setLink(link);
                img.setDeleteHash(deleteHash);
                img.setUser(user);

                // Save the image in the database and return it
                return imageRepository.save(img);
            } else {
                throw new RuntimeException("Imgur API upload failed.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error uploading image to Imgur: " + e.getMessage());
        }
    }

    /**
     * Get all images associated with a specific user.
     *
     * @param user the user whose images need to be fetched
     * @return a list of Image objects belonging to the user
     */
    public List<Image> getImagesByUser(User user) {
        return imageRepository.findByUser(user);
    }

    /**
     * Delete an image from Imgur and remove its metadata from the database.
     *
     * @param imgurId the Imgur ID of the image to delete
     * @param user    the user requesting the deletion
     * @throws RuntimeException if the image is not found or the deletion fails
     */
    public void deleteImage(String deleteHash, User user) {
        // Retrieve the image from the database
        Image image = imageRepository.findByDeleteHashAndUser(deleteHash, user)
                .orElseThrow(() -> new RuntimeException("Image not found."));

        // Set the Authorization header with Client-ID for Imgur API
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Client-ID " + imgurConfig.getImgurClientId());

        // Create an HTTP entity with the headers
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        try {
            // Send a DELETE request to Imgur API
            String deleteUrl = IMGUR_API_URL + "/" + image.getDeleteHash();
            restTemplate.exchange(deleteUrl, HttpMethod.DELETE, requestEntity, Void.class);

            // Remove the image from the database
            imageRepository.delete(image);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting image from Imgur: " + e.getMessage());
        }
    }

    /**
     * Get a single image by Imgur ID and User.
     *
     * @param imgurId the Imgur ID of the image
     * @param user    the User who owns the image
     * @return the Image object if found
     * @throws RuntimeException if the image is not found
     */
    public Image getImageByImgurIdAndUser(String imgurId, User user) {
        return imageRepository.findOneByImgurIdAndUser(imgurId, user)
                .orElseThrow(() -> new RuntimeException("Image not found for this user."));
    }
}
