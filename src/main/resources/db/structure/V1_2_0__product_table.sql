CREATE TABLE product
(
    id                 BINARY(16) PRIMARY KEY UNIQUE,
    upc_code           VARCHAR(50)  NOT NULL,
    supplier           VARCHAR(50)  NOT NULL,
    brand              VARCHAR(50)  NOT NULL,
    name               VARCHAR(50)  NOT NULL UNIQUE,
    description        VARCHAR(254) NOT NULL,
    category           VARCHAR(50)  NOT NULL,
    sub_category       VARCHAR(50)  NOT NULL,
    status             VARCHAR(50)  NOT NULL,
    -- Auditing fields
    created_by         VARCHAR(50)  NOT NULL,
    created_date       TIMESTAMP(6) NOT NULL,
    last_modified_by   VARCHAR(50)  NOT NULL,
    last_modified_date TIMESTAMP(6) NOT NULL,
    version            INTEGER DEFAULT 0
);

CREATE INDEX idx_prd_upc ON product (upc_code);
CREATE INDEX idx_prd_supplier ON product (supplier);
CREATE INDEX idx_prd_brand ON product (brand);
CREATE INDEX idx_prd_name ON product (name);
CREATE INDEX idx_prd_description ON product (description);
CREATE INDEX idx_prd_category ON product (category);
CREATE INDEX idx_prd_sub_category ON product (sub_category);
CREATE INDEX idx_prd_status ON product (status);
