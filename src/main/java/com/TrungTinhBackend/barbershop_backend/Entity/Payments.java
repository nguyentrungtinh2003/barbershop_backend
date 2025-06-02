package com.TrungTinhBackend.barbershop_backend.Entity;

import com.TrungTinhBackend.barbershop_backend.Enum.PaymentMethod;
import com.TrungTinhBackend.barbershop_backend.Enum.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private Users customer;

    @OneToOne
    @JoinColumn(name = "appointment_id", referencedColumnName = "id")
    private Appointments appointments;

    private Double amount;

    private PaymentStatus paymentStatus;

    private PaymentMethod paymentMethod;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
