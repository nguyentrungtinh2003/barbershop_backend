package com.TrungTinhBackend.barbershop_backend.DTO;

import com.TrungTinhBackend.barbershop_backend.Enum.RoleEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RegisterDTO {

    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDateTime birthDay;
    private String img;
    private RoleEnum roleEnum;
    private String description;
}
