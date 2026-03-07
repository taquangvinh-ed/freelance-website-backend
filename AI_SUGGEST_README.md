# AI Suggest Endpoint (`/api/projects/ai-suggest`)

## Purpose
Generate a project draft suggestion from a free-text brief using Claude.

## Request
`POST /api/projects/ai-suggest`

Headers:
- `Authorization: Bearer <JWT>`
- `Content-Type: application/json`

Body:
```json
{
  "prompt": "Build a booking app for gym classes",
  "locale": "vi-VN",
  "currency": "USD",
  "clientContext": {
    "companyName": "FitCo",
    "industry": "Fitness",
    "projectType": "mobile",
    "preferredTimezone": "GMT+7",
    "budgetPreference": "medium"
  }
}
```

## Response Envelope
The endpoint returns `ResponseDTO`:
```json
{
  "statusCode": "200",
  "statusMessage": "AI suggestion generated successfully",
  "body": {
    "suggestedProject": {
      "title": "...",
      "description": "...",
      "categoryName": "...",
      "skillNames": ["..."],
      "scope": {
        "duration": "1 to 3 months",
        "level": "Intermediate",
        "workload": "Flexible"
      },
      "budget": {
        "type": "FIXED_PRICE",
        "currencyUnit": "USD",
        "fixedValue": 1200
      },
      "advancedPreferences": {
        "hoursPerWeek": "10to20",
        "isRemote": true,
        "preferredTimezone": "GMT+7",
        "minYearsExperience": 2,
        "preferredLanguages": ["English"],
        "preferVerified": false,
        "preferTopRated": false
      },
      "confidence": 0.82,
      "notes": ["..."]
    },
    "warnings": ["..."],
    "requestId": "ai_req_..."
  }
}
```

## Config
In environment variables:
- `ANTHROPIC_API_KEY=<your_key>`

Optional app config (`application-dev.yml`):
- `ai.claude.api-url`
- `ai.claude.model`
- `ai.claude.timeout-seconds`

## Notes
- Rate limited to **10 requests/hour/user**.
- If Claude fails, API returns a safe fallback draft with warnings.

