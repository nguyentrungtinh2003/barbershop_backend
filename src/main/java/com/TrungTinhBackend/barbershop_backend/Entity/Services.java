package com.TrungTinhBackend.barbershop_backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String img;

    private Double price;

    private Integer duration;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean isDeleted;

    @ManyToMany(mappedBy = "services",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JsonIgnore
    private List<Appointments> appointments;

    @ManyToMany(mappedBy = "services",cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JsonIgnore
    private Set<Shops> shops = new HashSet<>();

}
