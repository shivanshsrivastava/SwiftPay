package com.walletsystem.swiftpay.auth.service.impl;

import com.walletsystem.swiftpay.auth.entity.RefreshToken;
import com.walletsystem.swiftpay.auth.entity.User;
import com.walletsystem.swiftpay.auth.repository.RefreshTokenRepository;
import com.walletsystem.swiftpay.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl
        implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenDuration;

    @Override
    public RefreshToken createRefreshToken(User user) {

        refreshTokenRepository.findByUserId(user.getId())
                .ifPresent(refreshTokenRepository::delete);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(
                        LocalDateTime.now()
                                .plusSeconds((refreshTokenDuration / 1000))
                )
                .revoked(false)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }
}