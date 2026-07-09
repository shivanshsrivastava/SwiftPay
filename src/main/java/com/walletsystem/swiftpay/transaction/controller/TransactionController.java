package com.walletsystem.swiftpay.transaction.controller;

import com.walletsystem.swiftpay.common.response.ApiResponse;
import com.walletsystem.swiftpay.transaction.dto.request.TransferRequest;
import com.walletsystem.swiftpay.transaction.dto.response.TransactionDetailsResponse;
import com.walletsystem.swiftpay.transaction.dto.response.TransactionHistoryResponse;
import com.walletsystem.swiftpay.transaction.dto.response.TransferResponse;
import com.walletsystem.swiftpay.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor

public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ApiResponse<TransferResponse> transfer(
            @Valid @RequestBody TransferRequest request
    ) {

//        throw new RuntimeException("I AM INSIDE TRANSFER CONTROLLER");

        TransferResponse response =
                transactionService.transfer(request);

        return ApiResponse.<TransferResponse>builder()
                .success(true)
                .message("Transfer successful")
                .data(response)
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<List<TransactionHistoryResponse>>
    getMyTransactions() {

        List<TransactionHistoryResponse> response =
                transactionService.getMyTransactions();

        return ApiResponse
                .<List<TransactionHistoryResponse>>builder()
                .success(true)
                .message("Transactions retrieved successfully")
                .data(response)
                .build();
    }

    @GetMapping("/{reference}")
    public ApiResponse<TransactionDetailsResponse>
    getTransactionDetails(
            @PathVariable String reference
    ) {

        TransactionDetailsResponse response =
                transactionService
                        .getTransactionDetails(reference);

        return ApiResponse
                .<TransactionDetailsResponse>builder()
                .success(true)
                .message("Transaction retrieved successfully.")
                .data(response)
                .build();
    }
}
