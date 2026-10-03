-- =====================================================================
-- Equity Trading App : TRADE table
-- Target DB          : PostgreSQL 12+  (uses a STORED generated column)
-- Run AFTER 01_create_customer_table.sql (FK -> customer)
-- =====================================================================

CREATE TABLE IF NOT EXISTS trade (
    trade_id          BIGSERIAL       PRIMARY KEY,
    trade_ref_no      VARCHAR(30)     NOT NULL UNIQUE,          -- exchange / broker trade reference
    order_id          VARCHAR(30)     NOT NULL,
    customer_id       BIGINT          NOT NULL,
    symbol            VARCHAR(20)     NOT NULL,                  -- e.g. INFY, TCS, RELIANCE
    isin              VARCHAR(12),
    exchange          VARCHAR(10)     NOT NULL DEFAULT 'NSE',
    side              VARCHAR(4)      NOT NULL,                  -- BUY / SELL
    order_type        VARCHAR(10)     NOT NULL DEFAULT 'MARKET',
    product_type      VARCHAR(10)     NOT NULL DEFAULT 'CNC',    -- CNC=delivery, MIS=intraday
    quantity          INTEGER         NOT NULL,
    price             NUMERIC(15, 4)  NOT NULL,
    trade_value       NUMERIC(20, 4)  GENERATED ALWAYS AS (quantity * price) STORED,
    brokerage         NUMERIC(12, 4)  NOT NULL DEFAULT 0,
    taxes             NUMERIC(12, 4)  NOT NULL DEFAULT 0,        -- STT, GST, stamp duty etc.
    currency          CHAR(3)         NOT NULL DEFAULT 'INR',
    trade_status      VARCHAR(15)     NOT NULL DEFAULT 'EXECUTED',
    trade_time        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    trade_date        DATE            NOT NULL DEFAULT CURRENT_DATE,
    settlement_date   DATE,
    created_at        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_trade_customer
        FOREIGN KEY (customer_id) REFERENCES customer (customer_id),
    CONSTRAINT chk_trade_side
        CHECK (side IN ('BUY', 'SELL')),
    CONSTRAINT chk_trade_order_type
        CHECK (order_type IN ('MARKET', 'LIMIT', 'SL', 'SL-M')),
    CONSTRAINT chk_trade_product_type
        CHECK (product_type IN ('CNC', 'MIS', 'NRML')),
    CONSTRAINT chk_trade_exchange
        CHECK (exchange IN ('NSE', 'BSE')),
    CONSTRAINT chk_trade_status
        CHECK (trade_status IN ('EXECUTED', 'CANCELLED', 'SETTLED', 'FAILED')),
    CONSTRAINT chk_trade_quantity
        CHECK (quantity > 0),
    CONSTRAINT chk_trade_price
        CHECK (price > 0),
    CONSTRAINT chk_trade_settlement_date
        CHECK (settlement_date IS NULL OR settlement_date >= trade_date)
);

-- Indexes chosen for reporting queries (per-customer statements, symbol & daily reports)
CREATE INDEX IF NOT EXISTS idx_trade_customer_date ON trade (customer_id, trade_date);
CREATE INDEX IF NOT EXISTS idx_trade_symbol_date   ON trade (symbol, trade_date);
CREATE INDEX IF NOT EXISTS idx_trade_trade_date    ON trade (trade_date);
CREATE INDEX IF NOT EXISTS idx_trade_order_id      ON trade (order_id);
CREATE INDEX IF NOT EXISTS idx_trade_status        ON trade (trade_status);

COMMENT ON TABLE trade IS 'Executed equity trades of customers';
