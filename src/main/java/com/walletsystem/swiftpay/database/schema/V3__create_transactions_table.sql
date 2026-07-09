CREATE TABLE transactions (

                              id BIGINT PRIMARY KEY AUTO_INCREMENT,

                              transaction_reference VARCHAR(100) NOT NULL UNIQUE,

                              sender_wallet_id BIGINT NOT NULL,

                              receiver_wallet_id BIGINT NOT NULL,

                              amount DECIMAL(19,2) NOT NULL,

                              status VARCHAR(20) NOT NULL,

                              created_at TIMESTAMP NOT NULL
                                  DEFAULT CURRENT_TIMESTAMP,

                              CONSTRAINT fk_tx_sender
                                  FOREIGN KEY (sender_wallet_id)
                                      REFERENCES wallets(id),

                              CONSTRAINT fk_tx_receiver
                                  FOREIGN KEY (receiver_wallet_id)
                                      REFERENCES wallets(id)
);
