-- One-time, reviewed repair for demo rows created by the old Zoom seeder.
-- Run with a backup and capture the dry-run result before applying the UPDATE.
-- This intentionally matches only the generated demo-consultation prefix.

START TRANSACTION;

-- Dry run: record these rows before the update.
SELECT id, appointment_id, join_url, start_url
FROM appointment_detail
WHERE join_url LIKE 'https://example.com/demo-consultation/%'
   OR start_url LIKE 'https://example.com/demo-consultation/%';

-- Clear each known placeholder independently so a real link in the other
-- column is preserved. The statement is idempotent.
UPDATE appointment_detail
SET join_url = CASE
                   WHEN join_url LIKE 'https://example.com/demo-consultation/%' THEN NULL
                   ELSE join_url
               END,
    start_url = CASE
                    WHEN start_url LIKE 'https://example.com/demo-consultation/%' THEN NULL
                    ELSE start_url
                END
WHERE join_url LIKE 'https://example.com/demo-consultation/%'
   OR start_url LIKE 'https://example.com/demo-consultation/%';

SELECT ROW_COUNT() AS repaired_rows;

-- Review the affected rows and COMMIT only after the result is correct.
-- COMMIT;
-- ROLLBACK;
