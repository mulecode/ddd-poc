CREATE TABLE product
(
    id                  BINARY(16) PRIMARY KEY UNIQUE,
    code                VARCHAR(50)  NOT NULL UNIQUE,
    manufacturer        VARCHAR(50)  NOT NULL,
    supplier            VARCHAR(50)  NOT NULL,
    brand               VARCHAR(50)  NOT NULL,
    name                VARCHAR(50)  NOT NULL UNIQUE,
    description         VARCHAR(254) NOT NULL,
    category            VARCHAR(50)  NOT NULL,
    sub_category        VARCHAR(50)  NOT NULL,
    origin_country_code VARCHAR(2)   NOT NULL,
    status              VARCHAR(50)  NOT NULL,
    -- Auditing fields
    created_by          VARCHAR(50)  NOT NULL,
    created_date        TIMESTAMP(6) NOT NULL,
    last_modified_by    VARCHAR(50)  NOT NULL,
    last_modified_date  TIMESTAMP(6) NOT NULL,
    version             INTEGER DEFAULT 0
);

CREATE INDEX idx_prd_code ON product (code);
CREATE INDEX idx_prd_manufac ON product (manufacturer);
CREATE INDEX idx_prd_supplier ON product (supplier);
CREATE INDEX idx_prd_brand ON product (brand);
CREATE INDEX idx_prd_name ON product (name);
CREATE INDEX idx_prd_description ON product (description);
CREATE INDEX idx_prd_category ON product (category);
CREATE INDEX idx_prd_sub_category ON product (sub_category);
CREATE INDEX idx_prd_origin_country ON product (origin_country_code);
CREATE INDEX idx_prd_status ON product (status);


CREATE TABLE product_variation
(
    id                 BINARY(16) PRIMARY KEY UNIQUE,
    product_id         BINARY(16) NOT NULL,
    upc_code           VARCHAR(12)  NOT NULL UNIQUE,
    name               VARCHAR(50)  NOT NULL UNIQUE,
    description        VARCHAR(254) NOT NULL,
    status             VARCHAR(50)  NOT NULL,
    -- Auditing fields
    created_by         VARCHAR(50)  NOT NULL,
    created_date       TIMESTAMP(6) NOT NULL,
    last_modified_by   VARCHAR(50)  NOT NULL,
    last_modified_date TIMESTAMP(6) NOT NULL,
    version            INTEGER DEFAULT 0,

    CONSTRAINT fk_prod_var FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE INDEX idx_prv_prdid ON product_variation (product_id);
CREATE INDEX idx_prv_name ON product_variation (name);
CREATE INDEX idx_prv_upc ON product_variation (upc_code);
