package com.walletsystem.swiftpay.idempotency.dto;

import com.walletsystem.swiftpay.idempotency.enums.IdempotencyStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdempotencyRecord {

    private IdempotencyStatus status;

    private Object response;

}