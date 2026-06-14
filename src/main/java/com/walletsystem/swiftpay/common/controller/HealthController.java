package com.walletsystem.swiftpay.common.controller;

import com.walletsystem.swiftpay.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/health")
    public ApiResponse<String> healthCheck() {

            return ApiResponse.<String>builder()
                .success(true)
                .message("Application is running successfully")
                .data("SwiftPay Backend Active")
                .build();
    }
}
