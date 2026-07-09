package com.walletsystem.swiftpay.wallet.repository;

import com.walletsystem.swiftpay.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository
        extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByWalletNumber(String walletNumber);

    Optional<Wallet> findByUserId(Long userId);
    boolean existsByUserId(Long userId);
}
