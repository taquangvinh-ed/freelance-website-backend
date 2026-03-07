# 🧪 POSTMAN TEST SCENARIO - AI PROJECT ASSISTANT

## 📋 PREREQUISITE

### 1. Backend Running
```bash
cd /home/tqvinh/Workspaces/freelancer-marketplace/freelance-website-backend
./gradlew bootRun
```

### 2. Have User Account
You need a user account in database with:
- Email: `user@example.com`
- Password: `password`
- Role: `CLIENT` or `FREELANCER`

---

## 🎯 TEST SCENARIO (Step-by-Step)

### TEST 1: Health Check (No Auth) ✅

**Purpose**: Verify AI service is up

**Request**:
```
Method: GET
URL: http://localhost:8080/api/ai/project-assistant/health
Headers: (none required)
```

**Expected Response (200 OK)**:
```json
{
  "status": "UP",
  "service": "AI Project Assistant",
  "timestamp": "2026-03-06T10:00:00Z"
}
```

**Postman Setup**:
1. Create new request
2. Name: "AI - Health Check"
3. Method: GET
4. URL: `http://localhost:8080/api/ai/project-assistant/health`
5. Click **Send**

---

### TEST 2: Login to Get Token 🔑

**Purpose**: Get JWT token for authenticated requests

**Request**:
```
Method: POST
URL: http://localhost:8080/api/login
Headers:
  Content-Type: application/json
Body (raw JSON):
{
  "email": "user@example.com",
  "password": "password"
}
```

**Expected Response (200 OK)**:
```json
{
  "statusCode": "200",
  "statusMessage": "Request successful",
  "body": "eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiQ0xJRU5UIiwidXNlcklkIjoxLCJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNzA5NzE2ODAwLCJleHAiOjE3MDk3Nzc2ODB9.signature"
}
```

**Postman Setup**:
1. Create new request
2. Name: "Login"
3. Method: POST
4. URL: `http://localhost:8080/api/login`
5. Headers:
   - `Content-Type: application/json`
6. Body → raw → JSON:
   ```json
   {
     "email": "user@example.com",
     "password": "password"
   }
   ```
7. Click **Send**
8. **COPY the token from response body** (the long string starting with `eyJ`)

---

### TEST 3: Set Token as Environment Variable 🔐

**Purpose**: Reuse token for all requests

**Postman Setup**:
1. Click **Environments** (left sidebar)
2. Create new environment: "Freelance Marketplace"
3. Add variable:
   - Variable: `token`
   - Type: `default`
   - Initial Value: `<paste token from TEST 2>`
   - Current Value: `<paste token from TEST 2>`
4. Click **Save**
5. Select this environment from dropdown (top-right)

---

### TEST 4: Generate AI Suggestions ✨

**Purpose**: Test main AI endpoint

**Request**:
```
Method: POST
URL: http://localhost:8080/api/ai/project-assistant/suggest
Headers:
  Authorization: Bearer {{token}}
  Content-Type: application/json
Body (raw JSON):
{
  "brief": "Tôi cần xây dựng website thương mại điện tử bán quần áo online, có tích hợp thanh toán, quản lý kho hàng, và dashboard admin",
  "categoryId": 1,
  "scope": "MEDIUM",
  "timeline": "1 to 3 months",
  "experienceLevel": "INTERMEDIATE",
  "preferredSkills": ["React", "Node.js", "PostgreSQL", "Stripe"]
}
```

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "AI suggestion generated successfully",
  "timestamp": "2026-03-06T10:30:00Z",
  "body": {
    "requestId": "ai_req_01HXYZ123",
    "projectDraft": {
      "title": "E-commerce Website Development for Online Clothing Store",
      "description": "Develop a comprehensive e-commerce platform for selling clothes online...",
      "categoryId": 1,
      "scope": "MEDIUM",
      "skills": ["React", "Node.js", "PostgreSQL", "Stripe", "Express.js", "Redux"],
      "timelineDays": 60
    },
    "budgetSuggestion": {
      "currency": "USD",
      "min": 3000,
      "recommended": 5000,
      "max": 8000,
      "confidence": 0.82,
      "pricingSource": "market_stats_v1",
      "marketSummary": "Based on 245 similar projects in Web Development",
      "factors": {
        "scopeWeight": 1.0,
        "complexityWeight": 1.2,
        "experienceWeight": 1.0,
        "urgencyWeight": 1.0,
        "locationWeight": 1.0
      }
    },
    "advancedPreferencesSuggestion": {
      "experienceLevel": "INTERMEDIATE",
      "projectDuration": 60,
      "hoursPerWeek": 20,
      "freelancerType": "INDIVIDUAL",
      "locationPreference": "ANY",
      "timezoneOverlap": "2-4h",
      "englishLevel": "CONVERSATIONAL",
      "projectType": "ONE_TIME",
      "complexity": "MEDIUM",
      "urgent": false
    },
    "clarifyingQuestions": [
      "Do you need mobile app (iOS/Android) in addition to website?",
      "What payment methods besides Stripe do you want to support?",
      "Do you need multi-language support?"
    ],
    "warnings": []
  }
}
```

**Postman Setup**:
1. Create new request
2. Name: "AI - Generate Suggestions"
3. Method: POST
4. URL: `http://localhost:8080/api/ai/project-assistant/suggest`
5. Headers:
   - `Authorization: Bearer {{token}}`
   - `Content-Type: application/json`
6. Body → raw → JSON: (paste body từ trên)
7. Click **Send**
8. **SAVE recommendationId from response** for next tests

---

### TEST 5: Improve Suggestion 🔧

**Purpose**: Refine AI suggestion based on feedback

**Request**:
```
Method: POST
URL: http://localhost:8080/api/ai/project-assistant/improve/{recommendationId}
Headers:
  Authorization: Bearer {{token}}
  Content-Type: application/json
Body (raw JSON):
{
  "feedback": "Tôi muốn thêm tính năng chat real-time với khách hàng và hệ thống đánh giá sản phẩm 5 sao"
}
```

**Replace `{recommendationId}`** with ID from TEST 4 response (e.g., `123`)

**Expected Response (200 OK)**:
```json
{
  "success": true,
  "message": "AI suggestion improved successfully",
  "timestamp": "2026-03-06T10:35:00Z",
  "body": {
    "requestId": "ai_req_01HXYZ456",
    "projectDraft": {
      "title": "E-commerce Website with Real-time Chat & Rating System",
      "description": "Enhanced e-commerce platform with live chat support and 5-star product review system...",
      "skills": ["React", "Node.js", "PostgreSQL", "Stripe", "Socket.io", "Redis"],
      "timelineDays": 75
    },
    "budgetSuggestion": {
      "min": 4000,
      "recommended": 6500,
      "max": 10000,
      "confidence": 0.85
    },
    "clarifyingQuestions": [
      "Do you want customer support agents to manage chat or AI chatbot?",
      "Should users be able to upload photos in reviews?"
    ]
  }
}
```

**Postman Setup**:
1. Create new request
2. Name: "AI - Improve Suggestion"
3. Method: POST
4. URL: `http://localhost:8080/api/ai/project-assistant/improve/{{recommendationId}}`
5. Headers: (same as TEST 4)
6. Body → raw → JSON: (paste body từ trên)
7. Add environment variable:
   - Variable: `recommendationId`
   - Value: `<ID from TEST 4>`
8. Click **Send**

---

### TEST 6: Submit Feedback 📊

**Purpose**: Record user satisfaction

**Request**:
```
Method: POST
URL: http://localhost:8080/api/ai/project-assistant/feedback/{recommendationId}
Headers:
  Authorization: Bearer {{token}}
  Content-Type: application/json
Body (raw JSON):
{
  "userFeedback": "ACCEPTED",
  "feedbackNotes": "Gợi ý rất hữu ích và chính xác, tôi sẽ sử dụng để đăng dự án"
}
```

**userFeedback values**: `ACCEPTED`, `REJECTED`, `PARTIAL`, `NOT_PROVIDED`

**Expected Response (200 OK)**:
```json
{
  "statusCode": "200",
  "statusMessage": "Request successful",
  "body": "Feedback recorded successfully"
}
```

**Postman Setup**:
1. Create new request
2. Name: "AI - Submit Feedback"
3. Method: POST
4. URL: `http://localhost:8080/api/ai/project-assistant/feedback/{{recommendationId}}`
5. Headers: (same as TEST 4)
6. Body → raw → JSON: (paste body từ trên)
7. Click **Send**

---

### TEST 7: Get Recommendation History 📜

**Purpose**: View past AI suggestions

**Request**:
```
Method: GET
URL: http://localhost:8080/api/ai/project-assistant/history?page=0&size=10
Headers:
  Authorization: Bearer {{token}}
```

**Expected Response (200 OK)**:
```json
{
  "statusCode": "200",
  "statusMessage": "Request successful",
  "body": [
    {
      "recommendationId": 123,
      "userBrief": "Tôi cần xây dựng website...",
      "suggestedTitle": "E-commerce Website Development...",
      "suggestedBudgetRecommended": 5000,
      "userFeedback": "ACCEPTED",
      "createdAt": "2026-03-06T10:30:00Z"
    },
    {
      "recommendationId": 122,
      "userBrief": "Build mobile app...",
      "suggestedTitle": "Mobile App Development...",
      "suggestedBudgetRecommended": 8000,
      "userFeedback": "PARTIAL",
      "createdAt": "2026-03-05T15:20:00Z"
    }
  ]
}
```

**Postman Setup**:
1. Create new request
2. Name: "AI - Get History"
3. Method: GET
4. URL: `http://localhost:8080/api/ai/project-assistant/history?page=0&size=10`
5. Headers:
   - `Authorization: Bearer {{token}}`
6. Click **Send**

---

### TEST 8: Get Usage Statistics 📈

**Purpose**: View AI usage stats

**Request**:
```
Method: GET
URL: http://localhost:8080/api/ai/project-assistant/stats
Headers:
  Authorization: Bearer {{token}}
```

**Expected Response (200 OK)**:
```json
{
  "statusCode": "200",
  "statusMessage": "Request successful",
  "body": {
    "totalRecommendations": 15,
    "acceptedCount": 8,
    "rejectedCount": 2,
    "partialCount": 3,
    "pendingFeedback": 2,
    "averageBudget": 5500,
    "totalTokensUsed": 45000,
    "totalCostUSD": 12.50,
    "remainingRequestsThisHour": 7
  }
}
```

**Postman Setup**:
1. Create new request
2. Name: "AI - Get Stats"
3. Method: GET
4. URL: `http://localhost:8080/api/ai/project-assistant/stats`
5. Headers:
   - `Authorization: Bearer {{token}}`
6. Click **Send**

---

## 🚨 ERROR SCENARIOS (Test These Too)

### ERROR 1: 401 Unauthorized (No Token)

**Request**:
```
Method: POST
URL: http://localhost:8080/api/ai/project-assistant/suggest
Headers: (NO Authorization header)
Body: (same as TEST 4)
```

**Expected Response (401)**:
```json
{
  "success": false,
  "message": "User not authenticated",
  "timestamp": "2026-03-06T10:00:00Z"
}
```

---

### ERROR 2: 401 Unauthorized (Expired Token)

**Request**:
Use an old/expired token in Authorization header

**Expected Response (401)**:
```json
{
  "success": false,
  "message": "Tên đăng nhập hoặc mật khẩu không đúng.",
  "timestamp": "2026-03-06T10:00:00Z"
}
```

**Fix**: Login again to get new token

---

### ERROR 3: 400 Bad Request (Invalid Data)

**Request**:
```
Method: POST
URL: http://localhost:8080/api/ai/project-assistant/suggest
Headers: Authorization: Bearer {{token}}
Body:
{
  "brief": "",  // Empty brief
  "categoryId": null,
  "scope": "INVALID_SCOPE"
}
```

**Expected Response (400)**:
```json
{
  "success": false,
  "message": "Validation failed: brief cannot be empty, scope must be SMALL/MEDIUM/LARGE",
  "timestamp": "2026-03-06T10:00:00Z"
}
```

---

### ERROR 4: 429 Too Many Requests (Rate Limit)

**Request**:
Make 11 requests within 1 hour (limit is 10)

**Expected Response (429)**:
```json
{
  "success": false,
  "message": "Rate limit exceeded. You can make 10 requests per hour. Please try again later.",
  "timestamp": "2026-03-06T10:00:00Z",
  "body": {
    "retryAfter": 3540,
    "resetTime": "2026-03-06T11:00:00Z"
  }
}
```

---

## 📦 POSTMAN COLLECTION (Import Ready)

### Create Collection
1. Open Postman
2. Click **Import** → **Raw text**
3. Paste JSON below:

```json
{
  "info": {
    "name": "AI Project Assistant",
    "description": "Complete test suite for AI Project Assistant endpoints",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "1. Health Check",
      "request": {
        "method": "GET",
        "header": [],
        "url": {
          "raw": "http://localhost:8080/api/ai/project-assistant/health",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "ai", "project-assistant", "health"]
        }
      }
    },
    {
      "name": "2. Login",
      "event": [
        {
          "listen": "test",
          "script": {
            "exec": [
              "var jsonData = pm.response.json();",
              "pm.environment.set('token', jsonData.body);"
            ],
            "type": "text/javascript"
          }
        }
      ],
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"email\": \"user@example.com\",\n  \"password\": \"password\"\n}"
        },
        "url": {
          "raw": "http://localhost:8080/api/login",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "login"]
        }
      }
    },
    {
      "name": "3. Generate Suggestions",
      "event": [
        {
          "listen": "test",
          "script": {
            "exec": [
              "var jsonData = pm.response.json();",
              "if (jsonData.body && jsonData.body.requestId) {",
              "  pm.environment.set('recommendationId', jsonData.body.requestId);",
              "}"
            ],
            "type": "text/javascript"
          }
        }
      ],
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Authorization",
            "value": "Bearer {{token}}"
          },
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"brief\": \"Tôi cần xây dựng website thương mại điện tử bán quần áo online\",\n  \"categoryId\": 1,\n  \"scope\": \"MEDIUM\",\n  \"timeline\": \"1 to 3 months\",\n  \"experienceLevel\": \"INTERMEDIATE\",\n  \"preferredSkills\": [\"React\", \"Node.js\", \"PostgreSQL\"]\n}"
        },
        "url": {
          "raw": "http://localhost:8080/api/ai/project-assistant/suggest",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "ai", "project-assistant", "suggest"]
        }
      }
    },
    {
      "name": "4. Improve Suggestion",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Authorization",
            "value": "Bearer {{token}}"
          },
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"feedback\": \"Thêm tính năng chat real-time\"\n}"
        },
        "url": {
          "raw": "http://localhost:8080/api/ai/project-assistant/improve/{{recommendationId}}",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "ai", "project-assistant", "improve", "{{recommendationId}}"]
        }
      }
    },
    {
      "name": "5. Submit Feedback",
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Authorization",
            "value": "Bearer {{token}}"
          },
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"userFeedback\": \"ACCEPTED\",\n  \"feedbackNotes\": \"Rất hữu ích\"\n}"
        },
        "url": {
          "raw": "http://localhost:8080/api/ai/project-assistant/feedback/{{recommendationId}}",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "ai", "project-assistant", "feedback", "{{recommendationId}}"]
        }
      }
    },
    {
      "name": "6. Get History",
      "request": {
        "method": "GET",
        "header": [
          {
            "key": "Authorization",
            "value": "Bearer {{token}}"
          }
        ],
        "url": {
          "raw": "http://localhost:8080/api/ai/project-assistant/history?page=0&size=10",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "ai", "project-assistant", "history"],
          "query": [
            {"key": "page", "value": "0"},
            {"key": "size", "value": "10"}
          ]
        }
      }
    },
    {
      "name": "7. Get Stats",
      "request": {
        "method": "GET",
        "header": [
          {
            "key": "Authorization",
            "value": "Bearer {{token}}"
          }
        ],
        "url": {
          "raw": "http://localhost:8080/api/ai/project-assistant/stats",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8080",
          "path": ["api", "ai", "project-assistant", "stats"]
        }
      }
    }
  ]
}
```

4. Click **Import**
5. Collection will appear in left sidebar

---

## ✅ TEST EXECUTION CHECKLIST

### Pre-Test Setup
- [ ] Backend running on port 8080
- [ ] User account exists in database
- [ ] Postman installed
- [ ] Environment variables set

### Test Sequence
1. [ ] TEST 1: Health Check (no auth) → 200 OK
2. [ ] TEST 2: Login → Get token → Save to environment
3. [ ] TEST 3: Verify token saved in environment
4. [ ] TEST 4: Generate Suggestions → 200 OK → Save recommendationId
5. [ ] TEST 5: Improve Suggestion → 200 OK
6. [ ] TEST 6: Submit Feedback → 200 OK
7. [ ] TEST 7: Get History → 200 OK → See previous requests
8. [ ] TEST 8: Get Stats → 200 OK → See usage metrics

### Error Tests
- [ ] ERROR 1: No token → 401
- [ ] ERROR 2: Expired token → 401
- [ ] ERROR 3: Invalid data → 400
- [ ] ERROR 4: Rate limit (11 requests) → 429

---

## 🎯 SUCCESS CRITERIA

✅ All tests pass with expected status codes
✅ Token authentication works
✅ AI suggestions are generated
✅ Data is persisted in database
✅ Rate limiting works
✅ Error handling is correct

---

## 📊 EXPECTED RESULTS SUMMARY

| Test | Status | Response Time | Key Metrics |
|------|--------|---------------|-------------|
| Health Check | 200 OK | < 100ms | Service UP |
| Login | 200 OK | < 500ms | Token received |
| Generate | 200 OK | < 5s | Suggestions generated |
| Improve | 200 OK | < 5s | Enhanced suggestions |
| Feedback | 200 OK | < 200ms | Feedback recorded |
| History | 200 OK | < 500ms | List returned |
| Stats | 200 OK | < 300ms | Metrics calculated |

---

## 🔍 DEBUGGING TIPS

### If 401 Error
1. Check token is saved in environment
2. Token format: `Bearer {{token}}`
3. Re-login to get fresh token
4. Check backend logs for JWT validation errors

### If 500 Error
1. Check backend logs
2. Verify database connection
3. Check AI_LLM_API_KEY is set
4. Ensure all required fields are provided

### If Timeout
1. Check backend is running
2. Increase timeout in Postman settings
3. Check AI API (Claude) is reachable

---

## 🎉 COMPLETE!

**You now have a complete Postman test suite!** 

Run tests in order to verify all functionality works correctly. 🚀

