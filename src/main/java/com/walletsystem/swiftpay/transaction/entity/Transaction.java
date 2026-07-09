package com.walletsystem.swiftpay.transaction.entity;

import com.walletsystem.swiftpay.wallet.entity.Wallet;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "transaction_reference",
            nullable = false,
            unique = true
    )
    private String transactionReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "sender_wallet_id",
            nullable = false
    )
    private Wallet senderWallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "receiver_wallet_id",
            nullable = false
    )
    private Wallet receiverWallet;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}