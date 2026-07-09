CREATE TABLE wallets (

                         id BIGINT PRIMARY KEY AUTO_INCREMENT,

                         user_id BIGINT NOT NULL UNIQUE,

                         wallet_number VARCHAR(50) NOT NULL UNIQUE,

                         balance DECIMAL(19,2) NOT NULL DEFAULT 0.00,

                         status VARCHAR(20) NOT NULL,

                         version BIGINT NOT NULL DEFAULT 0,

                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT fk_wallet_user
                             FOREIGN KEY (user_id)
                                 REFERENCES users(id)
);
