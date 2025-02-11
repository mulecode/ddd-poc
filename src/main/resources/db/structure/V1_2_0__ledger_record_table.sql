CREATE TABLE ledger
(
    id                    VARCHAR(36) PRIMARY KEY UNIQUE,
    previous_id           VARCHAR(36)  DEFAULT NULL,
    user_id               BINARY(16)    NOT NULL,
    payer_account_id      VARCHAR(100) DEFAULT NULL,
    payee_account_id      VARCHAR(100) DEFAULT NULL,
    linked_transaction_id VARCHAR(36)  DEFAULT NULL,
    reference_id          VARCHAR(100)   NOT NULL,

    amount                DECIMAL(19, 2) NOT NULL,
    transaction_type      VARCHAR(100)   NOT NULL,
    transaction_category  VARCHAR(100)   NOT NULL,
    balance_snapshot      DECIMAL(19, 2) NOT NULL,
    transaction_status    VARCHAR(100)   NOT NULL,

    metadata              VARCHAR(254)   NOT NULL,

    transaction_nonce     INTEGER      DEFAULT 0,
    transaction_hash      VARCHAR(250)   NOT NULL,

    created_at            TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,

    version               INTEGER      DEFAULT 0,

    CONSTRAINT fk_ledger_rec_user FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT fk_ledger_rec_prev_id FOREIGN KEY (previous_id) REFERENCES ledger (id)
);

CREATE INDEX idx_user_id ON ledger (user_id);
CREATE INDEX idx_transaction_type_status ON ledger (transaction_type, transaction_status);
CREATE INDEX idx_created_at ON ledger (created_at);
