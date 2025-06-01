package com.TrungTinhBackend.barbershop_backend.Entity;

import com.TrungTinhBackend.barbershop_backend.Enum.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Appointments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    private AppointmentStatus appointmentStatus;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Users customer;

    @ManyToOne
    @JoinColumn(name = "barber_id")
    private Users barber;

    private List<Services> services;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
