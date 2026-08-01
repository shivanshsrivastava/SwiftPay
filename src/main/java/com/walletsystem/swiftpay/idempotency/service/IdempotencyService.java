package com.walletsystem.swiftpay.idempotency.service;

import com.walletsystem.swiftpay.idempotency.dto.IdempotencyRecord;

import java.util.Optional;


public interface IdempotencyService {

    Optional<IdempotencyRecord> findByKey(String idempotencyKey);

//    void createInProgress(
//            String idempotencyKey
//    );

    void markCompleted(
            String idempotencyKey,
            Object response
    );

    void markFailed(
            String idempotencyKey
    );

    <T> T getResponse(
            IdempotencyRecord record,
            Class<T> responseType
    );

    boolean createInProgress(String idempotencyKey);

}

