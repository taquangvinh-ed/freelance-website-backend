# 📋 Unified API Contract - Frontend/Backend

## ✅ 8 Điểm Chuẩn Hóa

### 1️⃣ Unified Response Envelope

**Tất cả endpoints** (6 cái) dùng **chung 1 format**:

```json
{
  "success": true,
  "message": "String message",
  "timestamp": "2026-03-06T10:30:45Z",
  "errorCode": null,
  "retryable": false,
  "data": { ... },
  "metadata": { ... }
}
```

**Điểm mới**:
- ✅ `errorCode`: Mã lỗi chuẩn (RATE_LIMIT_EXCEEDED, INVALID_INPUT, etc.)
- ✅ `retryable`: Frontend biết có nên thử lại không
- ✅ `metadata`: Chứa pagination, tracing, rate limit info

**Error Response** (giống success, chỉ khác `success: false`):
```json
{
  "success": false,
  "message": "Rate limit exceeded",
  "timestamp": "2026-03-06T10:31:00Z",
  "errorCode": "RATE_LIMIT_EXCEEDED",
  "retryable": true,
  "data": null,
  "metadata": {
    "rateLimitReset": 1741254660
  }
}
```

---

### 2️⃣ Error Codes (Chuẩn)

| Error Code | HTTP Status | Retryable | Meaning |
|------------|-------------|-----------|---------|
| `RATE_LIMIT_EXCEEDED` | 429 | ✅ Yes | Vượt 10 requests/hour |
| `AI_TIMEOUT` | 504 | ✅ Yes | AI request timeout (> 30s) |
| `INVALID_INPUT` | 400 | ❌ No | Input validation failed |
| `INVALID_TOKEN` | 401 | ❌ No | JWT invalid/expired |
| `UNAUTHORIZED` | 401 | ❌ No | User not authenticated |
| `NOT_FOUND` | 404 | ❌ No | Resource không tìm thấy |
| `INTERNAL_ERROR` | 500 | ✅ Yes | Server error |
| `AI_ERROR` | 500 | ✅ Yes | AI generation failed |

**Frontend dùng errorCode để xử lý UX**:
```javascript
if (response.errorCode === 'RATE_LIMIT_EXCEEDED') {
  // Show: "Bạn đã dùng hết quota. Vui lòng chờ..."
  // Display countdown timer
} else if (response.retryable) {
  // Show: "Có lỗi, vui lòng thử lại"
  // Show retry button
}
```

---

### 3️⃣ Timeline Chuẩn

**Input** (Request - request dùng text, output dùng số):
```json
{
  "timeline": "1 to 3 months"  // Text như cũ (keep backward compat)
}
```

**Output** (Response - trả cả 2):
```json
{
  "projectDraft": {
    "timelineDays": 60,           // ✅ CHÍNH: Số ngày
    "timelineDisplay": "1 to 3 months"  // Hiển thị cho user
  }
}
```

**Mapping Table**:
| Input | Days | Display |
|-------|------|---------|
| `"Less than 1 month"` | 21 | Less than 1 month |
| `"1 to 3 months"` | 60 | 1 to 3 months |
| `"3 to 6 months"` | 120 | 3 to 6 months |
| `"More than 6 months"` | 210 | More than 6 months |

---

### 4️⃣ ID Chuẩn Cho Truy Vết

**`recommendationId`** - Chính (database ID):
- Dùng cho: Database operations, /improve, /feedback
- Cố định, không thay đổi

**`requestId`** - Phụ (tracing ID):
- Format: `ai_req_${uuid}`
- Dùng cho: Logging, tracing, debugging
- Mỗi lần gọi /suggest hoặc /improve là 1 requestId mới

**Frontend xử lý**:
```javascript
// Lưu recommendationId để /improve, /feedback
const { data } = response;
const recommendationId = data.recommendationId;  // From previous save

// Log request ID cho debugging
console.log('Request ID:', data.requestId);
```

---

### 5️⃣ Factors & Advanced Preferences (Full Schema)

**KHÔNG dùng `{ ... }` trong docs**. Liệt kê FULL:

**budgetSuggestion.factors**:
```json
{
  "scopeWeight": 1.0,
  "complexityWeight": 1.0,
  "experienceWeight": 1.0,
  "urgencyWeight": 1.0,
  "locationWeight": 1.0
}
```

**advancedPreferencesSuggestion**:
```json
{
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
}
```

---

### 6️⃣ Pagination cho /history

**Request**:
```
GET /api/ai/project-assistant/history?page=0&size=10
```

**Response**:
```json
{
  "success": true,
  "message": "Recommendations retrieved",
  "data": [
    { recommendationId, userBrief, suggestedTitle, ... }
  ],
  "metadata": {
    "pagination": {
      "page": 0,
      "size": 10,
      "totalElements": 25,
      "totalPages": 3,
      "hasNext": true,
      "hasPrevious": false
    }
  }
}
```

**Frontend dùng**:
```javascript
const { page, size, totalElements, totalPages, hasNext } = response.metadata.pagination;

// Hiển thị: "Showing 1-10 of 25"
const start = page * size + 1;
const end = Math.min((page + 1) * size, totalElements);
console.log(`Showing ${start}-${end} of ${totalElements}`);

// Load more button
if (hasNext) {
  showLoadMoreButton();
}
```

---

### 7️⃣ Rate Limit Headers

**Response Headers** (tất cả endpoints):
```
X-RateLimit-Limit: 10
X-RateLimit-Remaining: 8
X-RateLimit-Reset: 1741254660
```

**Giải thích**:
- `X-RateLimit-Limit`: Tổng requests allowed/hour = 10
- `X-RateLimit-Remaining`: Requests còn lại = 8
- `X-RateLimit-Reset`: Unix timestamp (seconds) khi reset = 1741254660

**Frontend parse**:
```javascript
const headers = response.headers;
const remaining = parseInt(headers['x-ratelimit-remaining']);
const limit = parseInt(headers['x-ratelimit-limit']);
const resetTime = parseInt(headers['x-ratelimit-reset']) * 1000; // Convert to ms

// Show usage: "You have 8/10 requests remaining"
console.log(`You have ${remaining}/${limit} requests remaining`);

// If remaining === 0, show countdown to reset
if (remaining === 0) {
  const timeUntilReset = resetTime - Date.now();
  console.log(`Reset in ${Math.round(timeUntilReset / 1000)} seconds`);
}
```

---

### 8️⃣ Endpoints Contract Summary

| Endpoint | Request | Response Data | Pagination |
|----------|---------|---------------|------------|
| **POST /suggest** | `ProjectAssistantRequest` | `ProjectSuggestionResponse` | N/A |
| **POST /improve/{id}** | `feedback: string` | `ProjectSuggestionResponse` | N/A |
| **POST /feedback/{id}** | `feedback: ACCEPTED\|REJECTED\|PARTIAL, notes: string` | Null | N/A |
| **GET /history** | `page=0&size=10` | `List<RecommendationHistory>` | ✅ Yes |
| **GET /stats** | N/A | `UserStats` | N/A |
| **GET /health** | N/A | `{status: "UP"}` | N/A |

---

## 📊 Request/Response Examples (Updated)

### Example 1: POST /suggest (Success)

**Request**:
```json
{
  "brief": "Build e-commerce website",
  "categoryId": 1,
  "scope": "MEDIUM",
  "timeline": "1 to 3 months",
  "experienceLevel": "INTERMEDIATE"
}
```

**Response**:
```json
{
  "success": true,
  "message": "AI suggestion generated successfully",
  "timestamp": "2026-03-06T10:30:45Z",
  "errorCode": null,
  "retryable": false,
  "data": {
    "requestId": "ai_req_a1b2c3d4e5f6",
    "projectDraft": {
      "title": "Professional E-Commerce Platform",
      "description": "...",
      "categoryId": 1,
      "scope": "MEDIUM",
      "skills": ["React", "Node.js"],
      "timelineDays": 60,
      "timelineDisplay": "1 to 3 months"
    },
    "budgetSuggestion": {
      "currency": "USD",
      "min": 3000,
      "recommended": 5000,
      "max": 8000,
      "confidence": 0.85,
      "pricingSource": "market_stats_v1",
      "marketSummary": "Based on 245 similar projects",
      "factors": {
        "scopeWeight": 1.0,
        "complexityWeight": 1.0,
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
    "clarifyingQuestions": ["Do you need payment gateway?"],
    "warnings": []
  },
  "metadata": {
    "requestId": "ai_req_a1b2c3d4e5f6"
  }
}
```

**Response Headers**:
```
X-RateLimit-Limit: 10
X-RateLimit-Remaining: 9
X-RateLimit-Reset: 1741254660
```

---

### Example 2: Rate Limit Error

**Response** (429):
```json
{
  "success": false,
  "message": "Rate limit exceeded. Maximum 10 requests per hour.",
  "timestamp": "2026-03-06T10:31:00Z",
  "errorCode": "RATE_LIMIT_EXCEEDED",
  "retryable": true,
  "data": null,
  "metadata": {
    "rateLimitReset": 1741254660,
    "requestsUsed": 10,
    "requestsLimit": 10
  }
}
```

**Response Headers**:
```
X-RateLimit-Limit: 10
X-RateLimit-Remaining: 0
X-RateLimit-Reset: 1741254660
```

---

### Example 3: GET /history (With Pagination)

**Response**:
```json
{
  "success": true,
  "message": "Recommendations retrieved",
  "timestamp": "2026-03-06T10:30:45Z",
  "data": [
    {
      "recommendationId": 1,
      "userBrief": "Build e-commerce...",
      "suggestedTitle": "Professional E-Commerce Platform",
      "suggestedBudgetMin": 3000,
      "suggestedBudgetRecommended": 5000,
      "suggestedBudgetMax": 8000,
      "userFeedback": "ACCEPTED",
      "createdAt": "2026-03-06T10:30:45Z"
    }
  ],
  "metadata": {
    "pagination": {
      "page": 0,
      "size": 10,
      "totalElements": 25,
      "totalPages": 3,
      "hasNext": true,
      "hasPrevious": false
    }
  }
}
```

---

## 🔧 Frontend Implementation (Updated)

### Parse Response

```javascript
async function callAI(request) {
  try {
    const response = await fetch('/api/ai/project-assistant/suggest', {
      method: 'POST',
      headers: { 'Authorization': `Bearer ${token}` },
      body: JSON.stringify(request)
    });
    
    const json = await response.json();
    
    // ✅ Check success
    if (!json.success) {
      handleError(json.errorCode, json.message, json.retryable);
      return;
    }
    
    // ✅ Use data
    const suggestion = json.data;
    console.log(suggestion.projectDraft.title);
    
    // ✅ Check rate limit headers
    const remaining = response.headers.get('x-ratelimit-remaining');
    console.log(`${remaining} requests remaining`);
    
  } catch (error) {
    console.error('Network error:', error);
  }
}

function handleError(errorCode, message, retryable) {
  switch (errorCode) {
    case 'RATE_LIMIT_EXCEEDED':
      showMessage('Quota exceeded. Please wait...');
      break;
    case 'INVALID_INPUT':
      showMessage('Invalid input. Please check your data.');
      break;
    default:
      if (retryable) {
        showMessage(`Error: ${message}. Click to retry.`);
        showRetryButton();
      } else {
        showMessage(`Error: ${message}`);
      }
  }
}
```

---

## ✅ Checklist Implement

- [ ] Backend: Use `UnifiedApiResponse<T>` cho ALL endpoints
- [ ] Backend: Add error codes (RATE_LIMIT_EXCEEDED, AI_TIMEOUT, etc.)
- [ ] Backend: Add rate limit headers (X-RateLimit-*)
- [ ] Backend: Return `timelineDays` + `timelineDisplay`
- [ ] Backend: Full pagination metadata trong /history
- [ ] Backend: ProjectSuggestionResponse với full schema
- [ ] Frontend: Parse `response.data` instead of `response.body`
- [ ] Frontend: Check `response.success` + `response.errorCode`
- [ ] Frontend: Handle retryable errors
- [ ] Frontend: Display rate limit remaining

---

**Đây là contract chuẩn! Frontend/Backend chạy mượt không bug.** ✅

