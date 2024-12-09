package com.example.imgurapp.repository;

import com.example.imgurapp.entity.Image;
import com.example.imgurapp.entity.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
    // Custom method to retrieve images by user
    List<Image> findByUser(User user);
}
