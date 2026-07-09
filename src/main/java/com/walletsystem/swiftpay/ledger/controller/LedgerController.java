package com.walletsystem.swiftpay.ledger.controller;

import com.walletsystem.swiftpay.common.response.ApiResponse;
import com.walletsystem.swiftpay.ledger.dto.LedgerEntryResponse;
import com.walletsystem.swiftpay.ledger.dto.LedgerHistoryResponse;
import com.walletsystem.swiftpay.ledger.service.LedgerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ledger")
@RequiredArgsConstructor
public class LedgerController {

    private final LedgerService ledgerService;

    @GetMapping("/me")
    public ApiResponse<List<LedgerHistoryResponse>>
    getMyLedgerHistory() {

        List<LedgerHistoryResponse> response =
                ledgerService.getMyLedgerHistory();

        return ApiResponse
                .<List<LedgerHistoryResponse>>builder()
                .success(true)
                .message("Ledger history retrieved successfully.")
                .data(response)
                .build();
    }

    @GetMapping("/transaction/{reference}")
    public ApiResponse<List<LedgerEntryResponse>>
    getLedgerEntriesByTransaction(
            @PathVariable String reference
    ) {

        List<LedgerEntryResponse> response =
                ledgerService.getLedgerEntriesByTransaction(
                        reference
                );

        return ApiResponse
                .<List<LedgerEntryResponse>>builder()
                .success(true)
                .message("Ledger entries retrieved successfully.")
                .data(response)
                .build();
    }

}