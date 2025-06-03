package com.TrungTinhBackend.barbershop_backend.DTO;

import com.TrungTinhBackend.barbershop_backend.Enum.RoleEnum;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {

    private Long id;

    private String username;

    private String email;

    private String phoneNumber;

    private String address;

    private LocalDateTime birthDay;

    private String img;

    private RoleEnum roleEnum;

    private String description;
}
