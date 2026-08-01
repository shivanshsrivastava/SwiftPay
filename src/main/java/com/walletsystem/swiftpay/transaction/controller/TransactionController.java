package com.walletsystem.swiftpay.transaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walletsystem.swiftpay.common.exception.IdempotencyException;
import com.walletsystem.swiftpay.common.response.ApiResponse;
import com.walletsystem.swiftpay.idempotency.dto.IdempotencyRecord;
import com.walletsystem.swiftpay.idempotency.service.IdempotencyService;
import com.walletsystem.swiftpay.transaction.dto.request.TransferRequest;
import com.walletsystem.swiftpay.transaction.dto.response.TransactionDetailsResponse;
import com.walletsystem.swiftpay.transaction.dto.response.TransactionHistoryResponse;
import com.walletsystem.swiftpay.transaction.dto.response.TransferResponse;
import com.walletsystem.swiftpay.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor

public class TransactionController {

    private final TransactionService transactionService;
    private final IdempotencyService idempotencyService;
    private final ObjectMapper objectMapper;

    @PostMapping("/transfer")
    public ApiResponse<TransferResponse> transfer(

            @RequestHeader(
                    value = "Idempotency-Key",
                    required = false
            )
            String idempotencyKey,

            @Valid
            @RequestBody
            TransferRequest request
    ) {

        // Idempotency flow
        if (idempotencyKey != null && !idempotencyKey.isBlank()) {

            Optional<IdempotencyRecord> existing =
                    idempotencyService.findByKey(idempotencyKey);

//            existing.ifPresent(this::handleExistingRequest);
            // ✅ We capture the response and exit the method immediately!
            if (existing.isPresent()) {
                return handleExistingRequest(existing.get());
            }

            boolean created =
                    idempotencyService.createInProgress(idempotencyKey);

            if (!created) {
                return handleExistingRequest(
                        idempotencyService.findByKey(idempotencyKey)
                                .orElseThrow()
                );
            }

            try {

                TransferResponse response =
                        transactionService.transfer(request);

                idempotencyService.markCompleted(
                        idempotencyKey,
                        response
                );

                return ApiResponse.<TransferResponse>builder()
                        .success(true)
                        .message("Transfer successful.")
                        .data(response)
                        .build();

            } catch (Exception ex) {

                idempotencyService.markFailed(idempotencyKey);

                throw ex;
            }
        }

        // Non-idempotent request
        TransferResponse response =
                transactionService.transfer(request);

        return ApiResponse.<TransferResponse>builder()
                .success(true)
                .message("Transfer successful.")
                .data(response)
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<List<TransactionHistoryResponse>> getMyTransactions() {

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
    public ApiResponse<TransactionDetailsResponse> getTransactionDetails(
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


    private ApiResponse<TransferResponse> handleExistingRequest(
            IdempotencyRecord record
    ) {

        switch (record.getStatus()) {

            case COMPLETED -> {

                TransferResponse response =
                        idempotencyService.getResponse(
                                record,
                                TransferResponse.class
                        );

                return ApiResponse.<TransferResponse>builder()
                        .success(true)
                        .message("Returning cached response.")
                        .data(response)
                        .build();
            }

            case IN_PROGRESS -> throw new IdempotencyException(
                    "Request is already being processed."
            );

            case FAILED -> throw new IdempotencyException(
                    "Previous request failed. Retry with a new Idempotency-Key."
            );

            default -> throw new IllegalStateException(
                    "Unexpected idempotency status: " + record.getStatus()
            );
        }
    }
}
