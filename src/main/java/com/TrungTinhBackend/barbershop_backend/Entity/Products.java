package com.TrungTinhBackend.barbershop_backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Products {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String img;
    private int price;
    private int stock;

    private LocalDate createdAt;
    private LocalDate updateAt;

    private boolean isDeleted;

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private List<CartItems> cartItems;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    @JsonIgnore
    private Shops shop;
}
