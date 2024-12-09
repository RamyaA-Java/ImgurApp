package com.example.imgurapp.repository;

import com.example.imgurapp.entity.Image;
import com.example.imgurapp.entity.User;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    // Custom method to retrieve images by user
    List<Image> findByUser(User user);

    // Find an image by its Imgur ID and associated user
    Optional<Image> findByImgurIdAndUser(String imgurId, User user);

    // Get a single image by user and imgurId
    Optional<Image> findOneByImgurIdAndUser(String imgurId, User user);

}
