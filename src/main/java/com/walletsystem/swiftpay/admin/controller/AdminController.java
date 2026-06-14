package com.walletsystem.swiftpay.admin.controller;

import com.walletsystem.swiftpay.security.util.SecurityUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    SecurityUtils securityUtils = new SecurityUtils();
    @GetMapping("/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminTest() {
        return "Admin endpoint accessed successfully";
    }

    @GetMapping("/whoami")
    public String whoAmI() {
        return securityUtils.getCurrentUserEmail();
    }
}
