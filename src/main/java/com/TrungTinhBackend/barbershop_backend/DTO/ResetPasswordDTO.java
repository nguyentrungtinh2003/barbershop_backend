package com.TrungTinhBackend.barbershop_backend.DTO;

import lombok.Data;

@Data
public class ResetPasswordDTO {

    private String email;
    private String otp;
    private String password;
}
