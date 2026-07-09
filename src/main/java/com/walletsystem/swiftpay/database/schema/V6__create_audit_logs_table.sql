CREATE TABLE audit_logs
(

    id          BIGINT AUTO_INCREMENT PRIMARY KEY,

    user_id     BIGINT,

    action      ENUM(
        'REGISTER',
        'LOGIN',
        'REFRESH_TOKEN',
        'CREATE_WALLET',
        'TRANSFER',
        'FREEZE_WALLET',
        'UNFREEZE_WALLET'
    ) NOT NULL,

    entity_type ENUM(
        'USER',
        'WALLET',
        'TRANSACTION',
        'LEDGER',
        'ADMIN'
    ) NOT NULL,

    entity_id   VARCHAR(100),

    description VARCHAR(500),

    status      ENUM(
        'SUCCESS',
        'FAILED'
    ) NOT NULL,

    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_audit_user
        FOREIGN KEY (user_id)
            REFERENCES users (id),

    INDEX       idx_audit_user (user_id),
    INDEX       idx_audit_created (created_at),
    INDEX       idx_audit_action (action)

);