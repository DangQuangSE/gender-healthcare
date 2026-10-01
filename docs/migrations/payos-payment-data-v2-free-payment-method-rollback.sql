-- PayOS payment data migration v2 rollback
-- Execute only if the deployment rollback checklist approves it.

-- Abort this rollback if PayOS or FREE payment rows exist; converting the enum
-- would otherwise make those rows unreadable or coerce them to an invalid value.
SELECT COUNT(*) AS payos_or_free_rows
FROM payment
WHERE method IN ('PAYOS', 'FREE');

-- Continue only when payos_or_free_rows = 0.
ALTER TABLE payment
    MODIFY COLUMN method ENUM('MOMO', 'PAY_OFF', 'PAYOS', 'VN_PAY') NULL;
