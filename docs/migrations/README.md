# Payment data migration runbook

`payos-payment-data-v1.sql` is a reviewed migration artifact, not an application-startup script. The repository currently has no Flyway or Liquibase migration directory, so deployment must execute this SQL through the approved database change process.

Before execution:

1. Back up the database and record counts for `payment` and `transaction`.
2. Verify the actual MySQL table/column names against the deployed schema.
3. Confirm the existing `payment.method` enum can be expanded with `PAYOS` and
   the previous application binary tolerates the additive nullable columns during
   rollback.
4. Run the script in staging and compare payment intent, charged amount and revenue reports.
5. Keep the rollback script and the backup available until the cutover window closes.

The migration deliberately keeps the historical `Payment.amount` report value and stores the calculated provider charge separately. Any change to revenue semantics requires a separate business decision and regression test.
