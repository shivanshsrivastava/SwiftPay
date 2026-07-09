package com.walletsystem.swiftpay.audit.service.impl;

import com.walletsystem.swiftpay.audit.dto.AuditHistoryResponse;
import com.walletsystem.swiftpay.audit.entity.AuditLog;
import com.walletsystem.swiftpay.audit.enums.AuditAction;
import com.walletsystem.swiftpay.audit.enums.AuditEntityType;
import com.walletsystem.swiftpay.audit.enums.AuditStatus;
import com.walletsystem.swiftpay.audit.mapper.AuditMapper;
import com.walletsystem.swiftpay.audit.repository.AuditRepository;
import com.walletsystem.swiftpay.audit.service.AuditService;
import com.walletsystem.swiftpay.audit.specification.AuditSpecification;
import com.walletsystem.swiftpay.auth.entity.User;
import com.walletsystem.swiftpay.common.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl
        implements AuditService {

    private final AuditRepository auditRepository;

    private final CurrentUserService currentUserService;

    private final AuditMapper auditMapper;

    @Override
    @Transactional
    public void recordAudit(
            AuditAction action,
            AuditEntityType entityType,
            String entityId,
            String description,
            AuditStatus status
    ) {

        User currentUser = currentUserService.getCurrentUser();

        recordAudit(
                currentUser,
                action,
                entityType,
                entityId,
                description,
                status
        );
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordAudit(
            User user,
            AuditAction action,
            AuditEntityType entityType,
            String entityId,
            String description,
            AuditStatus status
    ) {

        AuditLog auditLog = AuditLog.builder()
                .user(user)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .description(description)
                .status(status)
                .createdAt(LocalDateTime.now())
                .build();

        auditRepository.save(auditLog);

    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditHistoryResponse> getMyAuditHistory(AuditAction action, AuditStatus status, Pageable pageable) {
        User currentUser =
                currentUserService.getCurrentUser();

        Specification<AuditLog> specification =
                Specification
                        .where(
                                AuditSpecification.hasUserId(
                                        currentUser.getId()
                                )
                        )
                        .and(
                                AuditSpecification.hasAction(action)
                        )
                        .and(
                                AuditSpecification.hasStatus(status)
                        );

        Page<AuditLog> auditLogs =
                auditRepository.findAll(
                        specification,
                        pageable
                );

        return auditLogs.map(
                auditMapper::toHistoryResponse
        );
    }


//    @Override
//    @Transactional(readOnly = true)
//    public List<AuditHistoryResponse> getMyAuditHistory() {
//
//        User currentUser =
//                currentUserService.getCurrentUser();
//
//        return auditRepository
//                .findByUserIdOrderByCreatedAtDesc(
//                        currentUser.getId()
//                )
//                .stream()
//                .map(auditMapper::toHistoryResponse)
//                .toList();
//    }


}