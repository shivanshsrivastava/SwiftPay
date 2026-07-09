package com.walletsystem.swiftpay.common.service.impl;

import com.walletsystem.swiftpay.auth.entity.User;
import com.walletsystem.swiftpay.auth.repository.UserRepository;
import com.walletsystem.swiftpay.common.exception.UsernameNotFoundException;
import com.walletsystem.swiftpay.common.exception.WalletNotFoundException;
import com.walletsystem.swiftpay.common.service.CurrentUserService;
import com.walletsystem.swiftpay.security.util.SecurityUtils;
import com.walletsystem.swiftpay.wallet.entity.Wallet;
import com.walletsystem.swiftpay.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserServiceImpl implements CurrentUserService {

    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    @Override
    public User getCurrentUser() {
        String email = securityUtils.getCurrentUserEmail();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));
    }

    @Override
    public Wallet getCurrentUserWallet() {
        User user = getCurrentUser();

        return walletRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new WalletNotFoundException("Wallet not found"));
    }
}
