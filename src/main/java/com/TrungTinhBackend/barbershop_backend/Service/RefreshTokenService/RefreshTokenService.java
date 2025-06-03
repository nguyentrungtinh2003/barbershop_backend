package com.TrungTinhBackend.barbershop_backend.Service.RefreshTokenService;

import com.TrungTinhBackend.barbershop_backend.Entity.RefreshTokens;
import com.TrungTinhBackend.barbershop_backend.Entity.Users;

public interface RefreshTokenService {
    // Tạo refresh token mới
    public RefreshTokens createRefreshToken(String refreshToken, Users user);
    // Tìm refresh token trong database
    public RefreshTokens findByToken(String token);

    // Kiểm tra refresh token có hết hạn chưa
    public RefreshTokens verifyExpiration(RefreshTokens token);
}
