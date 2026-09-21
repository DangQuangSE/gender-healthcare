# PayOS cutover checklist

This checklist is the release gate for the VNPay-to-PayOS replacement. Local
fixtures and automated tests prove the application contract; they do not prove
merchant-account access or external webhook delivery.

## Checked in this workspace

- PayOS is the only payment provider in active backend configuration, API
  callers and booking UI.
- The exact public webhook route is
  `POST /api/v1/payments/payos/webhook`; creation and status routes remain
  authenticated.
- Full payment and the 20% deposit use separate payment intents and the same
  owner-scoped status contract.
- Historical `Payment.amount` remains the service amount; `Transaction.charged_amount`
  stores the actual provider charge.
- The reviewed migration and rollback scripts are under `docs/migrations/`.
- VNPay runtime classes, configuration keys, legacy callback routes, frontend
  asset and provider callers have been removed.

## Required before deployment

- [ ] Create a database backup and record `payment`/`transaction` row counts.
- [ ] Apply `docs/migrations/payos-payment-data-v1.sql` through the approved
      database change process and verify both postflight counts are zero.
- [ ] Configure `PAYOS_CLIENT_ID`, `PAYOS_API_KEY`, `PAYOS_CHECKSUM_KEY`,
      `PAYOS_RETURN_URL`, `PAYOS_CANCEL_URL` and an HTTPS `PAYOS_WEBHOOK_URL`
      outside Git.
- [ ] Run the documented PayOS `/confirm-webhook` procedure and retain the
      redacted result in the deployment record.
- [ ] Complete one full-payment and one 20% deposit sandbox payment. Verify the
      signed webhook, local terminal status and appointment/detail transition.
- [ ] Replay each valid webhook and verify no duplicate payment or appointment
      transition.
- [ ] Force a non-production persistence failure and verify provider retries do
      not produce a second local order or duplicate appointment transition.
- [ ] Keep the previous deployment binary, database backup and rollback script
      available for the agreed rollback window.

## Rollback

1. Stop new payment traffic at the deployment layer.
2. Revert the application to the previous known-good release.
3. If the schema migration must be reverted, execute the reviewed rollback SQL
   only after row counts and the old binary compatibility have been checked.
4. Do not re-enable a mixed provider state or mark browser return parameters as
   payment proof.

## Evidence status

The checked-out workspace has no PayOS credentials, merchant dashboard access or
reachable deployed HTTPS webhook, so sandbox completion and external webhook
confirmation remain operator-owned release gates. No production success is
claimed from local tests alone.
