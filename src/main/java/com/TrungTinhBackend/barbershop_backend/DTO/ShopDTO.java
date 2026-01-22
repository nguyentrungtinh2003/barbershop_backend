package com.TrungTinhBackend.barbershop_backend.DTO;

import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Data
public class ShopDTO {

    private Long id;

    private String name;

    private String phoneNumber;

    private String description;

    private String email;

    private String address;

    private String addressMap;

    private String img;

    private String slogan;

    private LocalDate createAt;

    private LocalDate updateAt;

    private boolean isDeleted;

    private Long ownerId;

    private String ownerName;

    private String ownerImg;

    private Set<Long> barbers;
}
