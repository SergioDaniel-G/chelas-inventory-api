package com.micheladas.chelas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class BeansConfig {

    /**
     * PROVIDE A BCRYPT PASSWORD ENCODER FOR SECURE PASSWORD HASHING
     **/

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}