-- =====================================================================
-- Equity Trading App : CUSTOMER table
-- Target DB          : PostgreSQL 12+
-- =====================================================================

CREATE TABLE IF NOT EXISTS customer (
    customer_id       BIGSERIAL       PRIMARY KEY,
    customer_code     VARCHAR(20)     NOT NULL UNIQUE,          -- client / trading account code
    first_name        VARCHAR(100)    NOT NULL,
    last_name         VARCHAR(100)    NOT NULL,
    email             VARCHAR(150)    NOT NULL UNIQUE,
    phone             VARCHAR(20),
    date_of_birth     DATE,
    pan_number        VARCHAR(10)     UNIQUE,                    -- tax id
    demat_account_no  VARCHAR(20)     UNIQUE,
    address_line1     VARCHAR(200),
    address_line2     VARCHAR(200),
    city              VARCHAR(100),
    state             VARCHAR(100),
    country           VARCHAR(60)     DEFAULT 'India',
    postal_code       VARCHAR(12),
    kyc_status        VARCHAR(20)     NOT NULL DEFAULT 'PENDING',
    account_status    VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE',
    risk_profile      VARCHAR(20)     DEFAULT 'MODERATE',
    created_at        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_customer_kyc_status
        CHECK (kyc_status IN ('PENDING', 'VERIFIED', 'REJECTED')),
    CONSTRAINT chk_customer_account_status
        CHECK (account_status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'CLOSED')),
    CONSTRAINT chk_customer_risk_profile
        CHECK (risk_profile IN ('LOW', 'MODERATE', 'HIGH'))
);

CREATE INDEX IF NOT EXISTS idx_customer_last_name      ON customer (last_name);
CREATE INDEX IF NOT EXISTS idx_customer_account_status ON customer (account_status);
CREATE INDEX IF NOT EXISTS idx_customer_created_at     ON customer (created_at);

COMMENT ON TABLE customer IS 'Customers / trading account holders of the equity trading app';
