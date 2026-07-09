package com.walletsystem.swiftpay.audit.dto;

import com.walletsystem.swiftpay.audit.enums.AuditAction;
import com.walletsystem.swiftpay.audit.enums.AuditEntityType;
import com.walletsystem.swiftpay.audit.enums.AuditStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditHistoryResponse {

    private AuditAction action;

    private AuditEntityType entityType;

    private String entityId;

    private String description;

    private AuditStatus status;

    private LocalDateTime createdAt;

}