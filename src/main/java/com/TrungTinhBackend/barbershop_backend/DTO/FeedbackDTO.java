package com.TrungTinhBackend.barbershop_backend.DTO;

import com.TrungTinhBackend.barbershop_backend.Entity.Users;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackDTO {

    private Long id;

    private Users customer;

    private Users barber;

    private String img;

    private Integer rating;

    private String comment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
