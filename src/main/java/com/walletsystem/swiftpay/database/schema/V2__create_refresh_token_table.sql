CREATE TABLE refresh_tokens (

    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    token VARCHAR(500) NOT NULL UNIQUE,

    user_id BIGINT NOT NULL UNIQUE,

    expiry_date TIMESTAMP NOT NULL,

    revoked BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_refresh_token_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);