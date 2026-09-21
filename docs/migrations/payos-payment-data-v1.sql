-- PayOS payment data migration v1
-- Review against the deployed MySQL schema before execution.
-- Execute only with a backup, row-count capture and an approved maintenance window.

-- Preflight
SELECT COUNT(*) AS payment_rows FROM payment;
SELECT COUNT(*) AS transaction_rows FROM `transaction`;
SELECT method, COUNT(*) AS row_count FROM payment GROUP BY method;

-- The existing MySQL enum must accept the active provider before new rows are written.
-- Preserve all historical values while adding PAYOS.
ALTER TABLE payment
    MODIFY COLUMN method ENUM('MOMO', 'PAY_OFF', 'PAYOS', 'VN_PAY') NULL;

ALTER TABLE payment
    ADD COLUMN payment_intent VARCHAR(32) NULL;

ALTER TABLE `transaction`
    ADD COLUMN provider_order_code BIGINT NULL,
    ADD COLUMN provider_payment_link_id VARCHAR(128) NULL,
    ADD COLUMN provider_checkout_url VARCHAR(512) NULL,
    ADD COLUMN charged_amount DECIMAL(19, 0) NULL;

-- Preserve the current business interpretation for existing records.
-- Existing PAY_OFF rows are deposits; other historical rows are full-payment intents.
UPDATE payment
SET payment_intent = CASE
    WHEN method = 'PAY_OFF' THEN 'DEPOSIT'
    ELSE 'FULL'
END
WHERE payment_intent IS NULL;

-- Preserve the current report amount while making the provider charge explicit.
UPDATE `transaction` t
JOIN payment p ON p.id = t.payment_id
SET t.charged_amount = CASE
    WHEN p.payment_intent = 'DEPOSIT' THEN FLOOR(p.amount * 0.20)
    ELSE p.amount
END
WHERE t.charged_amount IS NULL;

CREATE UNIQUE INDEX uk_transaction_provider_order_code
    ON `transaction` (provider_order_code);

-- Postflight
SELECT COUNT(*) AS missing_payment_intent
FROM payment
WHERE payment_intent IS NULL;

SELECT COUNT(*) AS missing_charged_amount
FROM `transaction`
WHERE charged_amount IS NULL;
