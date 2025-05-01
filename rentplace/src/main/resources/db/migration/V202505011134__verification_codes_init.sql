CREATE TABLE verification_codes
(
    email      VARCHAR(255) PRIMARY KEY,
    code       VARCHAR(6)                  NOT NULL,
    expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);