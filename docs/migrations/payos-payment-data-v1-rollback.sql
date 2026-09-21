-- PayOS payment data migration v1 rollback
-- Execute only if the deployment rollback checklist approves it.
-- Capture row counts and confirm the previous application binary is ready first.

-- Abort this rollback if any PayOS payment rows exist; converting the enum would
-- otherwise make those rows unreadable or coerce them to an invalid value.
SELECT COUNT(*) AS payos_rows
FROM payment
WHERE method = 'PAYOS';

-- Continue only when payos_rows = 0.
ALTER TABLE payment
    MODIFY COLUMN method ENUM('MOMO', 'PAY_OFF', 'VN_PAY') NULL;

DROP INDEX uk_transaction_provider_order_code ON `transaction`;

ALTER TABLE `transaction`
    DROP COLUMN charged_amount,
    DROP COLUMN provider_checkout_url,
    DROP COLUMN provider_payment_link_id,
    DROP COLUMN provider_order_code;

ALTER TABLE payment
    DROP COLUMN payment_intent;
