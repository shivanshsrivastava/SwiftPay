package com.walletsystem.swiftpay.audit.mapper;

import com.walletsystem.swiftpay.audit.dto.AuditHistoryResponse;
import com.walletsystem.swiftpay.audit.entity.AuditLog;
import org.springframework.stereotype.Component;

@Component
public class AuditMapper {

    public AuditHistoryResponse toHistoryResponse(
            AuditLog auditLog
    ) {

        return AuditHistoryResponse.builder()
                .action(auditLog.getAction())
                .entityType(auditLog.getEntityType())
                .entityId(auditLog.getEntityId())
                .description(auditLog.getDescription())
                .status(auditLog.getStatus())
                .createdAt(auditLog.getCreatedAt())
                .build();
    }

}