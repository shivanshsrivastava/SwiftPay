package com.walletsystem.swiftpay.audit.specification;

import com.walletsystem.swiftpay.audit.entity.AuditLog;
import com.walletsystem.swiftpay.audit.enums.AuditAction;
import com.walletsystem.swiftpay.audit.enums.AuditStatus;
import org.springframework.data.jpa.domain.Specification;

public final class AuditSpecification {

    private AuditSpecification() {
    }

    public static Specification<AuditLog> hasUserId(Long userId) {

        return (root, query, cb) ->
                cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<AuditLog> hasAction(
            AuditAction action
    ) {

        return (root, query, cb) -> {

            if (action == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("action"), action);
        };
    }

    public static Specification<AuditLog> hasStatus(
            AuditStatus status
    ) {

        return (root, query, cb) -> {

            if (status == null) {
                return cb.conjunction();
            }

            return cb.equal(root.get("status"), status);
        };
    }

}