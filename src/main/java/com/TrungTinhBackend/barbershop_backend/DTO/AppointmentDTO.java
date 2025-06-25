package com.TrungTinhBackend.barbershop_backend.DTO;

import com.TrungTinhBackend.barbershop_backend.Entity.Payments;
import com.TrungTinhBackend.barbershop_backend.Entity.Services;
import com.TrungTinhBackend.barbershop_backend.Entity.Shops;
import com.TrungTinhBackend.barbershop_backend.Entity.Users;
import com.TrungTinhBackend.barbershop_backend.Enum.AppointmentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AppointmentDTO {

    private Long id;

    private AppointmentStatus appointmentStatus;

    private LocalDate date;

    private String timeSlot;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Double price;

    private Users customer;

    private Users barber;

    private Shops shop;

    private List<Services> services;

    private Payments payments;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
