package com.walletsystem.swiftpay.auth.service;

import com.walletsystem.swiftpay.auth.dto.request.LoginRequest;
import com.walletsystem.swiftpay.auth.dto.request.RefreshTokenRequest;
import com.walletsystem.swiftpay.auth.dto.request.RegisterRequest;
import com.walletsystem.swiftpay.auth.dto.response.AuthResponse;
import com.walletsystem.swiftpay.auth.dto.response.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(
            RefreshTokenRequest request
    );
}
