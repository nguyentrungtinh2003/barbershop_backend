package com.TrungTinhBackend.barbershop_backend.Entity;

import com.TrungTinhBackend.barbershop_backend.Enum.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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

    private LocalDate date;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private AppointmentStatus appointmentStatus;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Users customer;

    @ManyToOne
    @JoinColumn(name = "barber_id")
    @JsonIgnore
    private Users barber;

    @ManyToMany
    @JoinTable(name = "appointment_service",
    joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    @JsonIgnore
    private List<Services> services;

    @OneToOne(mappedBy = "appointments", cascade = CascadeType.ALL)
    private Payments payments;

    @ManyToOne()
    @JoinColumn(name = "shop_id")
    @JsonIgnore
    private Shops shop;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
