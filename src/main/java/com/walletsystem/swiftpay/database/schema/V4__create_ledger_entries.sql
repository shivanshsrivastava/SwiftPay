CREATE TABLE ledger_entries (

                                id BIGINT AUTO_INCREMENT PRIMARY KEY,

                                transaction_id BIGINT NOT NULL,

                                transaction_reference VARCHAR(50) NOT NULL,

                                wallet_id BIGINT NOT NULL,

                                entry_type ENUM('DEBIT', 'CREDIT') NOT NULL,

                                amount DECIMAL(19,2) NOT NULL,

                                balance_before DECIMAL(19,2) NOT NULL,

                                balance_after DECIMAL(19,2) NOT NULL,

                                created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                CONSTRAINT fk_ledger_transaction
                                    FOREIGN KEY (transaction_id)
                                        REFERENCES transactions(id),

                                CONSTRAINT fk_ledger_wallet
                                    FOREIGN KEY (wallet_id)
                                        REFERENCES wallets(id),

                                INDEX idx_ledger_wallet_id (wallet_id),

                                INDEX idx_ledger_transaction_id (transaction_id),

                                INDEX idx_ledger_transaction_reference (transaction_reference),

                                INDEX idx_ledger_created_at (created_at)

);