package com.TrungTinhBackend.barbershop_backend.DTO;

import com.TrungTinhBackend.barbershop_backend.Entity.Users;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackDTO {

    private Long id;

    private Long customerId;

    private String customerName;

    private String customerImg;

    private Long barberId;

    private String barberName;

    private String barberImg;

    private Long shopId;

    private String shopName;

    private String shopImg;

    private String img;

    private Integer rating;

    private String comment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
