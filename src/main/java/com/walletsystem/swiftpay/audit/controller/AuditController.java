package com.walletsystem.swiftpay.audit.controller;

import com.walletsystem.swiftpay.audit.dto.AuditHistoryResponse;
import com.walletsystem.swiftpay.audit.enums.AuditAction;
import com.walletsystem.swiftpay.audit.enums.AuditStatus;
import com.walletsystem.swiftpay.audit.service.AuditService;
import com.walletsystem.swiftpay.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/me")
    public ApiResponse<Page<AuditHistoryResponse>>
    getMyAuditHistory(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(required = false)
            AuditAction action,

            @RequestParam(required = false)
            AuditStatus status

    ) {

        Pageable pageable = PageRequest.of(

                page,

                size,

                Sort.by("createdAt").descending()

        );

        Page<AuditHistoryResponse> response =
                auditService.getMyAuditHistory(

                        action,

                        status,

                        pageable

                );

        return ApiResponse
                .<Page<AuditHistoryResponse>>builder()
                .success(true)
                .message("Audit history retrieved successfully.")
                .data(response)
                .build();
    }

}