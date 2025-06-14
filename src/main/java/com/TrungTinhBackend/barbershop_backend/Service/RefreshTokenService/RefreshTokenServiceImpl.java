package com.TrungTinhBackend.barbershop_backend.Service.RefreshTokenService;

import com.TrungTinhBackend.barbershop_backend.Entity.RefreshTokens;
import com.TrungTinhBackend.barbershop_backend.Entity.Users;
import com.TrungTinhBackend.barbershop_backend.Repository.RefreshTokensRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{

    @Autowired
    private RefreshTokensRepository refreshTokensRepository;

    public static final long REFRESH_TOKEN_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; // 7 ngày (7 * 24 giờ * 60 phút * 60 giây * 1000 mili giây)

    public RefreshTokenServiceImpl(RefreshTokensRepository refreshTokenRepository) {
        this.refreshTokensRepository = refreshTokenRepository;
    }

    @Override
    public RefreshTokens createRefreshToken(String refreshToken, Users user) {
        // Tìm token hiện có
        RefreshTokens optionalRefreshToken = refreshTokensRepository.findByUser(user);
        RefreshTokens tokenToSave;

        if (optionalRefreshToken != null) {
            // Nếu token tồn tại -> Cập nhật
            optionalRefreshToken.setToken(refreshToken);
            optionalRefreshToken.setExpiryDate(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME));
            tokenToSave = optionalRefreshToken;
        } else {
            // Nếu chưa tồn tại -> Tạo mới
            RefreshTokens newToken = new RefreshTokens();
            newToken.setToken(refreshToken);
            newToken.setUser(user);
            newToken.setExpiryDate(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME));
            tokenToSave = newToken;
        }

        // Lưu và trả về token
        return refreshTokensRepository.save(tokenToSave);
    }

    @Override
    public RefreshTokens findByToken(String token) {
        return refreshTokensRepository.findByToken(token);
    }

    @Override
    public RefreshTokens verifyExpiration(RefreshTokens token) {
        if(token.getExpiryDate().before(new Date())) {
            throw new RuntimeException("Refresh token invalid !");
        }
        return token;
    }
}
