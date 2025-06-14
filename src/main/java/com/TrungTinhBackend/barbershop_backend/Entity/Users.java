package com.TrungTinhBackend.barbershop_backend.Entity;

import com.TrungTinhBackend.barbershop_backend.Enum.RoleEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    private String address;

    private LocalDateTime birthDay;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String img;

    private RoleEnum roleEnum;

    private String description;

    private boolean isDeleted;

    private String otp;

    private LocalDateTime otpExpiry;

    @OneToMany(mappedBy = "customer")
    private List<Payments> payments;

    @OneToMany(mappedBy = "customer")
    private List<Feedbacks> customerFeedbacks;

    @OneToMany(mappedBy = "barber")
    private List<Feedbacks> barberFeedbacks;

    @OneToMany(mappedBy = "customer")
    private List<Appointments> customerAppointments;

    @OneToMany(mappedBy = "barber")
    private List<Appointments> barberAppointments;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+roleEnum.name()));
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isDeleted;
    }
}
