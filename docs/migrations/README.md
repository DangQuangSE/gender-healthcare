# Payment data migration runbook

`payos-payment-data-v1.sql` and `payos-payment-data-v2-free-payment-method.sql` are reviewed migration artifacts, not application-startup scripts. Production deployment executes the approved v2 migration through `deploy/run-migrations.sh`, which creates a backup, records a checksum in `s_health_schema_migrations`, and runs each version at most once. The v1 script remains a manual baseline migration because existing installations already have its columns.

Migration order:

1. For a database that has not received the PayOS migration, run `payos-payment-data-v1.sql`.
2. For a database where v1 has already been run, production CI/CD runs `payos-payment-data-v2-free-payment-method.sql` before releasing the free-booking binary. A database where v2 was applied manually is adopted into the migration ledger without running the `ALTER TABLE` a second time.
3. Keep the matching rollback script with the migration record. The rollback scripts must not be executed while PayOS or FREE payment rows exist.

Before execution:

1. Back up the database and record counts for `payment` and `transaction`.
2. Verify the actual MySQL table/column names against the deployed schema.
3. Confirm the existing `payment.method` enum can be expanded with `PAYOS` and
   `FREE`, and the previous application binary tolerates the additive nullable
   columns during rollback.
4. Run the selected script in staging and compare payment intent, charged amount and revenue reports.
5. Verify `payment.method` contains `FREE` before enabling zero-price booking in production.
6. Keep the rollback script and the backup available until the cutover window closes.

The migration deliberately keeps the historical `Payment.amount` report value and stores the calculated provider charge separately. Any change to revenue semantics requires a separate business decision and regression test.
