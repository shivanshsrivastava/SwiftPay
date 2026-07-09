package com.walletsystem.swiftpay.wallet.controller;


import com.walletsystem.swiftpay.common.response.ApiResponse;
import com.walletsystem.swiftpay.wallet.dto.request.AddMoneyRequest;
import com.walletsystem.swiftpay.wallet.dto.response.BalanceResponse;
import com.walletsystem.swiftpay.wallet.dto.response.WalletResponse;
import com.walletsystem.swiftpay.wallet.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/createwallet")
    public ApiResponse<WalletResponse> createWallet() {

        WalletResponse response =
                walletService.createWallet();

        return ApiResponse.<WalletResponse>builder()
                .success(true)
                .message("Wallet created successfully")
                .data(response)
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<WalletResponse> getMyWallet() {

        WalletResponse response =
                walletService.getMyWallet();

        return ApiResponse.<WalletResponse>builder()
                .success(true)
                .message("Wallet retrieved successfully")
                .data(response)
                .build();
    }

    @GetMapping("/balance")
    public ApiResponse<BalanceResponse> getBalance() {

        BalanceResponse response =
                walletService.getBalance();

        return ApiResponse.<BalanceResponse>builder()
                .success(true)
                .message("Wallet balance retrieved successfully")
                .data(response)
                .build();
    }

    @PostMapping("/add-money")
    public ApiResponse<WalletResponse> addMoney(
            @Valid @RequestBody AddMoneyRequest addMoneyRequest
            ) {

        WalletResponse response =
                walletService.addMoney(addMoneyRequest);

        return ApiResponse.<WalletResponse>builder()
                .success(true)
                .message("Amount added successfully")
                .data(response)
                .build();
    }
}