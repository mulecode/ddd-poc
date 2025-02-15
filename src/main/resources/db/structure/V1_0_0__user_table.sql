CREATE TABLE user
(
    id                 BINARY(16) PRIMARY KEY UNIQUE,
    name               VARCHAR(100) NOT NULL,
    email              VARCHAR(100) NOT NULL,
    status             VARCHAR(100) NOT NULL,
    -- Auditing fields
    created_by         VARCHAR(50) DEFAULT NULL,
    created_date       TIMESTAMP   DEFAULT NULL,
    last_modified_by   VARCHAR(50) DEFAULT NULL,
    last_modified_date TIMESTAMP   DEFAULT NULL,
    version            INTEGER     DEFAULT 0
)
