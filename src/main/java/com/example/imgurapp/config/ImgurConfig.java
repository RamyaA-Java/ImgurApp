package com.example.imgurapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImgurConfig {

    @Value("${imgur.client.id}")
    private String imgurClientId;

    @Value("${imgur.client.secret}")
    private String imgurClientSecret;

    public String getImgurClientId() {
        return imgurClientId;
    }

    public String getImgurClientSecret() {
        return imgurClientSecret;
    }
}
