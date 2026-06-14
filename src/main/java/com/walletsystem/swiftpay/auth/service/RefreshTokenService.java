package com.walletsystem.swiftpay.auth.service;

import com.walletsystem.swiftpay.auth.entity.RefreshToken;
import com.walletsystem.swiftpay.auth.entity.User;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);
}
