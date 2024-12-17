CREATE TABLE points_ledger_records
(
    id                 VARCHAR(100) PRIMARY KEY UNIQUE,
    user_id            VARCHAR(100) NOT NULL,
    points             INTEGER   DEFAULT 0,
    transaction_type   VARCHAR(100) NOT NULL,
    description        VARCHAR(100) NOT NULL,
    system_description VARCHAR(100) NOT NULL,
    transaction_status VARCHAR(100) NOT NULL,
    balance            INTEGER   DEFAULT 0,
    version            INTEGER   DEFAULT 0,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    transaction_hash   VARCHAR(250) NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id)
)
