package com.walletsystem.swiftpay.wallet.service.impl;

import com.walletsystem.swiftpay.auth.entity.User;
import com.walletsystem.swiftpay.auth.repository.UserRepository;
import com.walletsystem.swiftpay.common.exception.UsernameNotFoundException;
import com.walletsystem.swiftpay.common.exception.WalletAlreadyExistsException;
import com.walletsystem.swiftpay.common.exception.WalletNotFoundException;
import com.walletsystem.swiftpay.common.util.WalletNumberGenerator;
import com.walletsystem.swiftpay.security.util.SecurityUtils;
import com.walletsystem.swiftpay.wallet.dto.request.AddMoneyRequest;
import com.walletsystem.swiftpay.wallet.dto.response.BalanceResponse;
import com.walletsystem.swiftpay.wallet.dto.response.WalletResponse;
import com.walletsystem.swiftpay.wallet.entity.Wallet;
import com.walletsystem.swiftpay.wallet.entity.enums.WalletStatus;
import com.walletsystem.swiftpay.wallet.repository.WalletRepository;
import com.walletsystem.swiftpay.wallet.service.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor

public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final WalletNumberGenerator walletNumberGenerator;


    @Override
    public WalletResponse createWallet() {
        String email = securityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));

        if (walletRepository.existsByUserId(user.getId())) {
            throw new WalletAlreadyExistsException("Wallet with user: " + user.getFullName() + " already exists.");
        }

     /*   Wallet wallet = walletRepository.existsByUserId(user.getId()).orElse(null);

        if (wallet != null) {
            throw new WalletAlreadyExistsException("Wallet with user: " + user.getFullName() + " already exists.");
        } else {
            // Redirect to registration page
        }

      */

        Wallet wallet = Wallet.builder().walletNumber(walletNumberGenerator.generateWalletNumber()).balance(BigDecimal.ZERO).status(WalletStatus.ACTIVE).createdAt(LocalDateTime.now()).user(user).build();

        Wallet wallet1 = walletRepository.save(wallet);
        return WalletResponse.builder().walletNumber(wallet1.getWalletNumber()).balance(wallet1.getBalance()).status(wallet1.getStatus()).build();
    }

    @Override
    public WalletResponse getMyWallet() {

//        Have created a private method "getCurrentUserWallet" to avoid code duplication.
//        String email =
//                securityUtils.getCurrentUserEmail();
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() ->
//                        new UsernameNotFoundException(
//                                "User not found"
//                        ));
//
//        Wallet wallet = walletRepository
//                .findByUserId(user.getId())
//                .orElseThrow(() ->
//                        new WalletNotFoundException(
//                                "Wallet not found"
//                        ));

        Wallet wallet = getCurrentUserWallet();
        return WalletResponse.builder().walletNumber(wallet.getWalletNumber()).balance(wallet.getBalance()).status(wallet.getStatus()).build();
    }

    @Override
    public BalanceResponse getBalance() {

        // Have created a private method "getCurrentUserWallet" to avoid code duplication
//        String email =
//                securityUtils.getCurrentUserEmail();
//
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() ->
//                        new UsernameNotFoundException(
//                                "User not found"
//                        ));
//
//        Wallet wallet = walletRepository
//                .findByUserId(user.getId())
//                .orElseThrow(() ->
//                        new WalletNotFoundException(
//                                "Wallet not found"
//                        ));


        Wallet wallet = getCurrentUserWallet();
        return BalanceResponse.builder().balance(wallet.getBalance()).build();
    }

    @Transactional
    public WalletResponse addMoney(
            AddMoneyRequest request
    ) {

        Wallet wallet = getCurrentUserWallet();

        wallet.setBalance(
                wallet.getBalance()
                        .add(request.getAmount())
        );

        Wallet updatedWallet =
                walletRepository.save(wallet);

       return WalletResponse.builder()
               .walletNumber(updatedWallet.getWalletNumber())
               .balance(updatedWallet.getBalance())
               .status(updatedWallet.getStatus())
               .build();
    }


    // Private helper method to avoid duplication of code
    private Wallet getCurrentUserWallet() {
        String email = securityUtils.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return walletRepository.findByUserId(user.getId())
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));
    }
}
