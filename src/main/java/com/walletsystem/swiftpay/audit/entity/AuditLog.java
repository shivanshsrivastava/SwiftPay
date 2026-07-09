package com.walletsystem.swiftpay.audit.entity;

import com.walletsystem.swiftpay.audit.enums.AuditAction;
import com.walletsystem.swiftpay.audit.enums.AuditEntityType;
import com.walletsystem.swiftpay.audit.enums.AuditStatus;
import com.walletsystem.swiftpay.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditAction action;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type", nullable = false)
    private AuditEntityType entityType;

    @Column(name = "entity_id")
    private String entityId;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}