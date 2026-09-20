# PayOS webhook runbook

The backend exposes exactly one public PayOS webhook endpoint:

```text
POST /api/v1/payments/payos/webhook
```

The endpoint is public to the JWT filter because PayOS cannot send the customer
JWT. It still requires a valid PayOS HMAC signature. The handler must return a
2xx response only after the webhook is valid and the local state transition is
accepted. Do not mark a payment successful from browser `returnUrl` or
`cancelUrl` parameters.

## Confirm the webhook with PayOS

Run this manually after the deployed HTTPS endpoint is reachable. Do not call
it from application startup and do not put real credentials in this repository.

```powershell
$headers = @{
  "x-client-id" = $env:PAYOS_CLIENT_ID
  "x-api-key" = $env:PAYOS_API_KEY
  "Content-Type" = "application/json"
}

Invoke-RestMethod `
  -Method Post `
  -Uri "https://api-merchant.payos.vn/confirm-webhook" `
  -Headers $headers `
  -Body (@{ webhookUrl = $env:PAYOS_WEBHOOK_URL } | ConvertTo-Json)
```

Expected result: PayOS returns code `00` and sends a sample webhook. Verify the
application logs contain only the local order/status outcome, never API keys,
checksum keys or the complete signed payload. Verify the sample delivery is
accepted once and a repeated delivery is idempotent.

The same operation can be performed in the PayOS merchant dashboard. The
configured URL must be HTTPS and must match `PAYOS_WEBHOOK_URL`.
