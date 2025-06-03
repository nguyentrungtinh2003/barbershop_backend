package com.TrungTinhBackend.barbershop_backend.Repository;

import com.TrungTinhBackend.barbershop_backend.Entity.RefreshTokens;
import com.TrungTinhBackend.barbershop_backend.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokensRepository extends JpaRepository<RefreshTokens,Long> {
    RefreshTokens findByUsers(Users user);
    RefreshTokens findByToken(String token);
}
