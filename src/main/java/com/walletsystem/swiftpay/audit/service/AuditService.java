package com.walletsystem.swiftpay.audit.service;

import com.walletsystem.swiftpay.audit.dto.AuditHistoryResponse;
import com.walletsystem.swiftpay.audit.enums.AuditAction;
import com.walletsystem.swiftpay.audit.enums.AuditEntityType;
import com.walletsystem.swiftpay.audit.enums.AuditStatus;
import com.walletsystem.swiftpay.auth.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditService {
    void recordAudit(

            AuditAction action,

            AuditEntityType entityType,

            String entityId,

            String description,

            AuditStatus status

    );


    void recordAudit(
            User user,
            AuditAction action,
            AuditEntityType entityType,
            String entityId,
            String description,
            AuditStatus status
    );

//
//    List<AuditHistoryResponse> getMyAuditHistory();
    Page<AuditHistoryResponse> getMyAuditHistory(

            AuditAction action,

            AuditStatus status,

            Pageable pageable

    );
}
