# Test AI Suggest Endpoint - Response Format Check

## 📝 Chuẩn Bị

### 1. Get JWT Token

```bash
# Login to get token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "client@example.com",
    "password": "password123"
  }'

# Response:
# {
#   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
#   ...
# }

export JWT_TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### 2. Verify Token Works

```bash
curl http://localhost:8080/api/freelancers/me \
  -H "Authorization: Bearer $JWT_TOKEN"

# Should return user info (not 401)
```

---

## ✅ Test Suggest Endpoint

### Request

```bash
curl -X POST http://localhost:8080/api/ai/project-assistant/suggest \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "brief": "I need to build an e-commerce website with payment integration and user authentication. Must support at least 1000 concurrent users.",
    "categoryId": 1,
    "scope": "LARGE",
    "timeline": "3 to 6 months",
    "experienceLevel": "EXPERT",
    "preferredSkills": ["React", "Node.js", "PostgreSQL"],
    "complexityHint": "HIGH"
  }' | jq .
```

### Expected Response Format

```json
{
  "success": true,
  "message": "AI suggestion generated successfully",
  "timestamp": "2026-03-06T10:30:45Z",
  "body": {
    "requestId": "ai_req_...",
    "projectDraft": {
      "title": "...",
      "description": "...",
      "categoryId": 1,
      "scope": "LARGE",
      "skills": ["React", "Node.js", "..."],
      "timelineDays": 120
    },
    "budgetSuggestion": {
      "currency": "USD",
      "min": 5000,
      "recommended": 12000,
      "max": 25000,
      "confidence": 0.87,
      "pricingSource": "market_stats_v1",
      "marketSummary": "Dựa trên ... dự án tương tự",
      "factors": {
        "scopeWeight": 1.0,
        "complexityWeight": 1.2,
        "experienceWeight": 1.0,
        "urgencyWeight": 1.0,
        "locationWeight": 1.0
      }
    },
    "advancedPreferencesSuggestion": {
      "experienceLevel": "EXPERT",
      "projectDuration": 120,
      "hoursPerWeek": 20,
      "freelancerType": "INDIVIDUAL",
      "locationPreference": "ANY",
      "timezoneOverlap": "2-4h",
      "englishLevel": "CONVERSATIONAL",
      "projectType": "ONE_TIME",
      "complexity": "HIGH",
      "urgent": false
    },
    "clarifyingQuestions": [
      "What payment gateways do you prefer?"
    ],
    "warnings": []
  }
}
```

---

## ✅ Checklist - Response Fields

Khi FE nhận response, check:

- [ ] `success === true`
- [ ] `message` có giá trị
- [ ] `timestamp` là ISO-8601 format
- [ ] `body.requestId` tồn tại
- [ ] `body.projectDraft.title` không rỗng
- [ ] `body.projectDraft.description` không rỗng
- [ ] `body.projectDraft.skills` là array
- [ ] `body.projectDraft.timelineDays > 0`
- [ ] `body.budgetSuggestion.min <= recommended <= max`
- [ ] `body.budgetSuggestion.confidence` giữa 0.0 và 1.0
- [ ] `body.budgetSuggestion.factors` có 5 key
- [ ] `body.advancedPreferencesSuggestion.complexity` là valid
- [ ] `body.clarifyingQuestions` là array
- [ ] `body.warnings` là array

---

## 🔍 Debugging

Nếu có lỗi, check:

```bash
# 1. Check server logs
docker logs freelancer-backend

# 2. Check JWT token còn hiệu lực không
curl http://localhost:8080/api/ai/project-assistant/health \
  -H "Authorization: Bearer $JWT_TOKEN"

# 3. Check database
psql -U postgres -d freelancer_db -c "SELECT * FROM ai_project_recommendations LIMIT 1;"

# 4. Check rate limiting
# (If error "Rate limit exceeded", wait 1 hour or change config)
```

---

## 📊 Response Mapping for Frontend

```javascript
// Get data from response
const { success, message, timestamp, body } = response;

// If success
if (success && body) {
  const {
    requestId,
    projectDraft,
    budgetSuggestion,
    advancedPreferencesSuggestion,
    clarifyingQuestions,
    warnings
  } = body;

  // Display to user
  console.log("Title:", projectDraft.title);
  console.log("Budget:", budgetSuggestion.recommended);
  console.log("Skills needed:", projectDraft.skills);
  console.log("Timeline:", projectDraft.timelineDays, "days");
  console.log("Confidence:", (budgetSuggestion.confidence * 100) + "%");
  console.log("Questions to answer:", clarifyingQuestions);
}
```

---

## 🚀 Success Indicator

Response format chính xác khi:
1. Không có `ResponseDTO` wrapper ngoài
2. Top-level keys: `success`, `message`, `timestamp`, `body`
3. Nested `body` chứa: `requestId`, `projectDraft`, `budgetSuggestion`, etc.
4. Tất cả các field có giá trị (không null/undefined)
5. HTTP Status 200 OK


