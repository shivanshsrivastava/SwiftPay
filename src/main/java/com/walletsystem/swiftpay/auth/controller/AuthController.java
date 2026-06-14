package com.walletsystem.swiftpay.auth.controller;

import com.walletsystem.swiftpay.auth.dto.request.LoginRequest;
import com.walletsystem.swiftpay.auth.dto.request.RefreshTokenRequest;
import com.walletsystem.swiftpay.auth.dto.request.RegisterRequest;
import com.walletsystem.swiftpay.auth.dto.response.AuthResponse;
import com.walletsystem.swiftpay.auth.dto.response.UserResponse;
import com.walletsystem.swiftpay.auth.service.AuthService;
import com.walletsystem.swiftpay.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor

// http://localhost:8080/swagger-ui/index.html
//{
//    "fullName": "praveen",
//        "email": "praveen@gmail.com",
//        "password": "praveen@123"
//}

public class AuthController {

    private final AuthService authService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        UserResponse response = authService.register(request);

        ApiResponse<UserResponse> body = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("User registered successfully")
                .data(response)
                .build();

        return ResponseEntity.ok(body);
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>>  login(
            @Valid @RequestBody LoginRequest request
    ) {

        AuthResponse response = authService.login(request);

        ApiResponse<AuthResponse> body = ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Login successful")
                .data(response)
                .build();

        return ResponseEntity.ok(body);
    }


    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request
    ) {

        AuthResponse response =
                authService.refreshToken(request);

        return ApiResponse.<AuthResponse>builder()
                .success(true)
                .message("Token refreshed successfully")
                .data(response)
                .build();
    }
}