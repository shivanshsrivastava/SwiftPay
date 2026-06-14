package com.walletsystem.swiftpay.auth.service.impl;

import com.walletsystem.swiftpay.auth.dto.request.LoginRequest;
import com.walletsystem.swiftpay.auth.dto.request.RefreshTokenRequest;
import com.walletsystem.swiftpay.auth.dto.request.RegisterRequest;
import com.walletsystem.swiftpay.auth.dto.response.AuthResponse;
import com.walletsystem.swiftpay.auth.dto.response.UserResponse;
import com.walletsystem.swiftpay.auth.entity.RefreshToken;
import com.walletsystem.swiftpay.auth.entity.Role;
import com.walletsystem.swiftpay.auth.entity.User;
import com.walletsystem.swiftpay.auth.repository.RefreshTokenRepository;
import com.walletsystem.swiftpay.auth.repository.UserRepository;
import com.walletsystem.swiftpay.auth.service.AuthService;
import com.walletsystem.swiftpay.auth.service.RefreshTokenService;
import com.walletsystem.swiftpay.common.exception.EmailAlreadyExistsException;
import com.walletsystem.swiftpay.common.exception.InvalidRefreshTokenException;
import com.walletsystem.swiftpay.security.jwt.JwtService;
import com.walletsystem.swiftpay.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "Email already registered"
            );
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .active(true)
                .createdAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(savedUser.getId())
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();
    }

//    @Override
//    public AuthResponse login(LoginRequest request) {
//
//        User user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow(() ->
//                        new RuntimeException("Invalid credentials"));
//
////        boolean passwordMatches = passwordEncoder.matches(
////                request.getPassword(),
////                user.getPassword()
////        );
//
//        // Authentication manager does the following
//        /*
//        UsernamePasswordAuthenticationToken
//                ↓
//        AuthenticationManager
//                ↓
//        DaoAuthenticationProvider
//                ↓
//        UserDetailsService.loadUserByUsername()
//                ↓
//        PasswordEncoder.matches()
//                ↓
//        Authentication Success/Failure
//         */
//
//        // Note to execute it properly we need UserDetailsService Bean, PasswordEncoder Bean,
//        // AuthenticationProvider Configured,
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
//
////        if (!passwordMatches) {
////            throw new RuntimeException("Invalid credentials");
////        }
//
//
//
//        String token = jwtService.generateToken(user.getEmail());
//
//        RefreshToken refreshToken =
//                refreshTokenService.createRefreshToken(user);
//
//        return AuthResponse.builder()
//                .accessToken(token)
//                .refreshToken(refreshToken.getToken())
//                .build();
//    }

    @Override
    public AuthResponse login(LoginRequest request) {

        // 1. The Circuit Breaker: This handles the DB lookup AND the password check.
        // It throws a BadCredentialsException if the email or password is wrong.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. If we reach this line, they are 100% authenticated.
        // We can extract the user details directly from the authentication object!
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // Assuming you add a simple 'public Users getUser()' getter to your CustomUserDetails class
        User authenticatedUser = userDetails.user();

        // 3. Generate Tokens using the verified user
        String token = jwtService.generateToken(authenticatedUser.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(authenticatedUser);

        return AuthResponse.builder()
                .accessToken(token)
                .refreshToken(refreshToken.getToken())
                .build();
    }


    @Override
    public AuthResponse refreshToken(
            RefreshTokenRequest request
    ) {

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() ->
                        new InvalidRefreshTokenException(
                                "Invalid refresh token"
                        ));

        if (refreshToken.getRevoked()) {
            throw new InvalidRefreshTokenException(
                    "Refresh token revoked"
            );
        }

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidRefreshTokenException(
                    "Refresh token expired"
            );
        }

        User user = refreshToken.getUser();

        String newAccessToken =
                jwtService.generateToken(user.getEmail());

        // ROTATE REFRESH TOKEN
        refreshToken.setRevoked(true);

        refreshTokenRepository.save(refreshToken);

        RefreshToken newRefreshToken =
                refreshTokenService.createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .build();
    }
}
