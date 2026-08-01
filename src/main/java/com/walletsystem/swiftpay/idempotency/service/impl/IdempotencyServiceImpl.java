package com.walletsystem.swiftpay.idempotency.service.impl;

import com.walletsystem.swiftpay.idempotency.dto.IdempotencyRecord;
import com.walletsystem.swiftpay.idempotency.enums.IdempotencyStatus;
import com.walletsystem.swiftpay.idempotency.service.IdempotencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class IdempotencyServiceImpl implements IdempotencyService {
//    private final IdempotencyKeyRepository repository;
//    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PREFIX =
            "IDEMPOTENCY:";
    private static final Duration TTL = Duration.ofHours(24);

    @Override
//    @Transactional(readOnly = true)
    public boolean createInProgress(String idempotencyKey) {

        ValueOperations<String, Object> operations =
                redisTemplate.opsForValue();

        IdempotencyRecord record =
                IdempotencyRecord.builder()
                        .status(IdempotencyStatus.IN_PROGRESS)
                        .build();

        Boolean created = operations.setIfAbsent(
                buildKey(idempotencyKey),
                record,
                TTL
        );
        return Boolean.TRUE.equals(created);
    }

    @Override
    public Optional<IdempotencyRecord> findByKey(
            String idempotencyKey
    ) {

        ValueOperations<String, Object> operations =
                redisTemplate.opsForValue();

        Object value =
                operations.get(buildKey(idempotencyKey));

        if (value == null) {
            return Optional.empty();
        }

        return Optional.of((IdempotencyRecord) value);
    }

    @Override
    public void markCompleted(
            String idempotencyKey,
            Object response
    ) {

        ValueOperations<String, Object> operations =
                redisTemplate.opsForValue();

        IdempotencyRecord existing =
                (IdempotencyRecord) operations.get(
                        buildKey(idempotencyKey)
                );

        if (existing == null) {
            throw new IllegalStateException(
                    "Idempotency key not found."
            );
        }

        IdempotencyRecord completed =
                IdempotencyRecord.builder()
                        .status(IdempotencyStatus.COMPLETED)
                        .response(response)
                        .build();

        operations.set(
                buildKey(idempotencyKey),
                completed,
                TTL
        );
    }

    @Override
    public void markFailed(String idempotencyKey) {

        ValueOperations<String, Object> operations =
                redisTemplate.opsForValue();

        String redisKey = buildKey(idempotencyKey);

        IdempotencyRecord existing =
                (IdempotencyRecord) operations.get(redisKey);

        if (existing == null) {
            return;
        }

        IdempotencyRecord failed =
                IdempotencyRecord.builder()
                        .status(IdempotencyStatus.FAILED)
                        .build();

        operations.set(
                redisKey,
                failed,
                TTL
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getResponse(
            IdempotencyRecord record,
            Class<T> responseType
    ) {
        return (T) record.getResponse();
    }


    private String buildKey(
            String idempotencyKey
    ) {

        return PREFIX + idempotencyKey;

    }


}
