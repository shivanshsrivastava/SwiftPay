package com.walletsystem.swiftpay.audit.repository;

import com.walletsystem.swiftpay.audit.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository
        extends JpaRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> {


}