CREATE TABLE ledger_account
(
    id           BINARY(16) PRIMARY KEY UNIQUE,
    user_id      BINARY(16)    NOT NULL,
    name         VARCHAR(50)  NOT NULL,
    description  VARCHAR(254) NOT NULL,
    status       VARCHAR(100) NOT NULL,
    account_type VARCHAR(100) NOT NULL,
    version      INTEGER DEFAULT 0,

    CONSTRAINT fk_ledger_acc_user FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE INDEX idx_user_id ON ledger_account (user_id);
CREATE INDEX idx_name ON ledger_account (name);

CREATE TABLE ledger_record
(
    id                   BINARY(16) PRIMARY KEY UNIQUE,
    payer_account_id     BINARY(16)    NOT NULL,
    payee_account_id     BINARY(16)    NOT NULL,
    reference_id         VARCHAR(50)    NOT NULL,
    amount               DECIMAL(19, 2) NOT NULL,
    transaction_type     VARCHAR(100)   NOT NULL,
    transaction_category VARCHAR(100)   NOT NULL,
    balance_snapshot     DECIMAL(19, 2) NOT NULL,
    version              INTEGER DEFAULT 0,

    CONSTRAINT fk_ledger_rec_payr FOREIGN KEY (payer_account_id) REFERENCES ledger_account (id),
    CONSTRAINT fk_ledger_rec_paye FOREIGN KEY (payee_account_id) REFERENCES ledger_account (id)
);
