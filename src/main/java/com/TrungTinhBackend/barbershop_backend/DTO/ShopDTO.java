package com.TrungTinhBackend.barbershop_backend.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ShopDTO {

    private Long id;

    private String name;

    private String phoneNumber;

    private String description;

    private String email;

    private String address;

    private String img;

    private String slogan;

    private LocalDate createAt;

    private LocalDate updateAt;

    private boolean isDeleted;
}
