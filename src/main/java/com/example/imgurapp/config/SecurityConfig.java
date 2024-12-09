package com.example.imgurapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring security settings...");

        http
                .csrf().disable() // Disable CSRF protection if not needed (optional)
                .authorizeRequests()
                .anyRequest().permitAll() // Allow all requests without authentication
                .and()
                .formLogin().disable() // Disable default login page
                .httpBasic().disable(); // Disable HTTP Basic Authentication (optional)

        logger.info("Security configuration completed: All requests are permitted without authentication.");

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
