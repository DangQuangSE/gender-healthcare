-- PayOS payment data migration v2: support zero-price services
-- Run after payos-payment-data-v1.sql when v1 was already applied.
-- Execute only with a backup, row-count capture and an approved maintenance window.

-- Preflight: confirm the deployed column and existing payment methods first.
SELECT COLUMN_TYPE
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME = 'payment'
  AND COLUMN_NAME = 'method';

SELECT method, COUNT(*) AS row_count
FROM payment
GROUP BY method;

-- Free bookings persist a successful zero-amount payment with method FREE.
ALTER TABLE payment
    MODIFY COLUMN method ENUM('MOMO', 'PAY_OFF', 'PAYOS', 'VN_PAY', 'FREE') NULL;

-- Postflight: verify the enum contains FREE before releasing the free-booking binary.
SELECT COLUMN_TYPE
FROM information_schema.COLUMNS
WHERE TABLE_SCHEMA = DATABASE()
  AND TABLE_NAME = 'payment'
  AND COLUMN_NAME = 'method';

SELECT COUNT(*) AS free_payment_rows
FROM payment
WHERE method = 'FREE';
