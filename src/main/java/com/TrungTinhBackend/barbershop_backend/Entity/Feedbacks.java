package com.TrungTinhBackend.barbershop_backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Feedbacks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "customer_id")
    private Users customer;

    @ManyToOne()
    @JoinColumn(name = "barber_id")
    private Users barber;

    @ManyToOne()
    @JoinColumn(name = "shop_id")
    private Shops shop;

    @OneToOne
    @JoinColumn(name = "appointment_id", unique = true)
    @JsonIgnore
    private Appointments appointment;

    private String img;

    private Integer rating;

    private String comment;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
