package com.TrungTinhBackend.barbershop_backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Shops {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phoneNumber;

    private String description;

    private String email;

    private String address;

    private String img;

    private String slogan;

    private LocalDate createAt;

    private LocalDate updateAt;

    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Users owner;

    @ManyToMany
    @JoinTable(
            name = "shop_barber",
            joinColumns = @JoinColumn(name = "shop_id"),
            inverseJoinColumns = @JoinColumn(name = "barber_id")
    )
    private Set<Users> barbers;
}
