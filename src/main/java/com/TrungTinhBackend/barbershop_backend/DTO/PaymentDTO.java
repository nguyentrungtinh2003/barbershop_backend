package com.TrungTinhBackend.barbershop_backend.DTO;

import com.TrungTinhBackend.barbershop_backend.Entity.Appointments;
import com.TrungTinhBackend.barbershop_backend.Entity.Users;
import com.TrungTinhBackend.barbershop_backend.Enum.PaymentMethod;
import com.TrungTinhBackend.barbershop_backend.Enum.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentDTO {
    private Long userId;

    @NotNull(message = "Price is required!")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0!")
    @DecimalMax(value = "1000000000", message = "Price must be less than 1 billion!")
    @Digits(integer = 10, fraction = 2, message = "Price must be a valid number with up to 2 decimal places!")
    private Double amount;

    private String method;
}
