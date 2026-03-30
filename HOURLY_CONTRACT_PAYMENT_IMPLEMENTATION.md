# Hourly Contract Payment - Implementation Notes

## What was implemented

This implementation adds an MVP hourly-payment pipeline on top of your existing weekly reporting flow.

### 1) Weekly report now triggers hourly payment creation
- File: `src/main/java/com/freelancemarketplace/backend/service/imp/ContractReportingServiceImp.java`
- After a weekly report is persisted (`WeeklyReportModel` + `WeeklyReportItemModel`), backend now calls:
  - `paymentService.createHourlyPaymentFromWeeklyReport(contract, reportWithItems)`
- This keeps the existing weekly summary email behavior and adds payment record creation in the same flow.

### 2) Payment service supports hourly payment from weekly report
- File: `src/main/java/com/freelancemarketplace/backend/service/PaymentService.java`
- Added new method:
  - `PaymentModel createHourlyPaymentFromWeeklyReport(ContractModel contract, WeeklyReportModel weeklyReport)`

### 3) Hourly payment creation logic (idempotent)
- File: `src/main/java/com/freelancemarketplace/backend/service/imp/PaymentServiceImp.java`
- Added method implementation with these rules:
  1. Validate `contract` and `weeklyReport` are not null and `weeklyReport` is persisted.
  2. Build idempotency key:
     - `transactionId = HOURLY-{contractId}-{weeklyReportId}`
  3. If a payment with same `transactionId` already exists, return existing payment (avoid duplicates).
  4. Calculate amount:
     - `grossAmount = contract.amount * weeklyReport.totalHours`
     - Round to 2 decimals.
  5. If amount <= 0, skip creating payment.
  6. Create `PaymentModel`:
     - `status = PENDING`
     - `type = CREDIT_CARD`
     - `transactionId` as above
     - link to `client`, `freelancer`, and `contract`

## Current workflow (runtime)

1. Hourly contract is active.
2. Scheduler triggers weekly report generation (existing lifecycle flow).
3. `generateWeeklyReport(...)` loads completed timelogs in time window.
4. Backend persists:
   - `WeeklyReportModel` (header)
   - `WeeklyReportItemModel` list (tasks)
5. Backend creates one payment record for that report using idempotency key.
6. Backend sends weekly summary email to client.

## Flowchart

```text
[Scheduler / Stop Contract]
          |
          v
[ContractReportingService.generateWeeklyReport]
          |
          v
[Load COMPLETED TimeLogs in [startTime, endTime]]
          |
          +--> (no logs) --> [Return null]
          |
          v
[Persist WeeklyReport + Items]
          |
          v
[PaymentService.createHourlyPaymentFromWeeklyReport]
          |
          +--> (transactionId exists) --> [Return existing payment]
          |
          +--> (amount <= 0) --> [Skip payment]
          |
          v
[Create PaymentModel(status=PENDING)]
          |
          v
[Send weekly summary email to client]
          |
          v
[Return WeeklyReportDTO]
```

## Notes and limitations of this MVP

- This implementation creates an internal payment record only (`PENDING`).
- It does **not** auto-charge Stripe yet for hourly invoices.
- Recommended next step:
  - Add Stripe off-session charge + transfer payout flow for these hourly records.
  - Add client review/dispute window before capture (Upwork-like behavior).

## Frontend endpoints added

Base URL: `/api`

1. `GET /contracts/{contractId}/hourly-payments`
   - Description: Get hourly payment records of a contract (newest first).
   - Roles: `CLIENT`, `FREELANCER`, `ADMIN` (must belong to contract by service check).

2. `GET /hourly-payments/{paymentId}`
   - Description: Get detail of one hourly payment record.
   - Roles: `CLIENT`, `FREELANCER`, `ADMIN` (must belong to contract by service check).

3. `PATCH /hourly-payments/{paymentId}/approve`
   - Description: Client approves weekly hourly payment.
   - Roles: `CLIENT`
   - Body (optional): `{ "note": "Approved" }`
   - Status transition: `PENDING -> COMPLETED`

4. `PATCH /hourly-payments/{paymentId}/dispute`
   - Description: Client disputes weekly hourly payment.
   - Roles: `CLIENT`
   - Body (optional): `{ "note": "Dispute reason" }`
   - Status transition: `PENDING -> FAILED`

## Files changed

- `src/main/java/com/freelancemarketplace/backend/service/PaymentService.java`
- `src/main/java/com/freelancemarketplace/backend/service/imp/PaymentServiceImp.java`
- `src/main/java/com/freelancemarketplace/backend/service/imp/ContractReportingServiceImp.java`

