package com.TrungTinhBackend.barbershop_backend.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                System.out.println("CORS Config Loaded");
                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://localhost:5173",
                                "https://localhost:3000"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("Authorization", "Content-Type", "Cookie")
                        .exposedHeaders("Set-Cookie")
                        .allowCredentials(true);
            }

        };
    }
}
