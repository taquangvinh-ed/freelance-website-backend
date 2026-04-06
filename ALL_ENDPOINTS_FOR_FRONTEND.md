# 🎯 AI Project Assistant - Tất Cả Endpoints Cho Frontend

## 📌 Base URL
```
http://localhost:8080/api/ai/project-assistant
```

---

## 1️⃣ Tạo Gợi Ý Dự Án (Main) ⭐

**Endpoint**: `POST /suggest`

**Full URL**: `http://localhost:8080/api/ai/project-assistant/suggest`

**Authorization**: ✅ JWT Token Required

**Headers**:
```
Content-Type: application/json
Authorization: Bearer {YOUR_JWT_TOKEN}
```

**Request Body**:
```json
{
  "brief": "Xây dựng website bán hàng online với thanh toán và quản lý user",
  "categoryId": 1,
  "scope": "MEDIUM",
  "timeline": "1 to 3 months",
  "experienceLevel": "INTERMEDIATE",
  "preferredSkills": ["React", "Node.js"],
  "region": null,
  "complexityHint": "MEDIUM"
}
```

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "AI suggestion generated successfully",
  "timestamp": "2026-03-06T10:30:45Z",
  "body": {
    "requestId": "ai_req_a1b2c3d4e5f6g7h8i9j0",
    "projectDraft": {
      "title": "Professional E-Commerce Platform Development",
      "description": "Build a modern e-commerce platform with React frontend and Node.js backend...",
      "categoryId": 1,
      "scope": "MEDIUM",
      "skills": ["React", "Node.js", "PostgreSQL", "Stripe API"],
      "timelineDays": 60
    },
    "budgetSuggestion": {
      "currency": "USD",
      "min": 3000,
      "recommended": 5000,
      "max": 8000,
      "confidence": 0.85,
      "pricingSource": "market_stats_v1",
      "marketSummary": "Dựa trên 245 dự án tương tự.",
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
    "clarifyingQuestions": [
      "Bạn có cần tích hợp cổng thanh toán nào cụ thể không?",
      "Bạn cần hỗ trợ nhiều tiền tệ không?"
    ],
    "warnings": []
  }
}
```

**Error Response (400)**:
```json
{
  "success": false,
  "message": "Rate limit exceeded. Maximum 10 requests per hour.",
  "timestamp": "2026-03-06T10:31:00Z",
  "body": null
}
```

---

## 2️⃣ Cải Thiện Gợi Ý

**Endpoint**: `POST /improve/{recommendationId}`

**Full URL**: `http://localhost:8080/api/ai/project-assistant/improve/1`

**Authorization**: ✅ JWT Token Required

**Headers**:
```
Content-Type: application/json
Authorization: Bearer {YOUR_JWT_TOKEN}
```

**Request Body**:
```json
{
  "feedback": "Giá quá cao, tôi chỉ có ngân sách $3000. Cũng không cần tích hợp cổng thanh toán phức tạp."
}
```

**Response (200 OK)**:
```json
{
  "success": true,
  "message": "AI suggestion generated successfully",
  "timestamp": "2026-03-06T10:31:10Z",
  "body": {
    "requestId": "ai_req_x9y8z7w6v5u4t3s2r1q0",
    "projectDraft": {
      "title": "Simple E-Commerce Website with Basic Payment",
      "description": "Build a simple e-commerce platform with basic Stripe payment...",
      "categoryId": 1,
      "scope": "MEDIUM",
      "skills": ["React", "Node.js", "PostgreSQL"],
      "timelineDays": 45
    },
    "budgetSuggestion": {
      "currency": "USD",
      "min": 2000,
      "recommended": 3000,
      "max": 4000,
      "confidence": 0.78,
      "pricingSource": "market_stats_v1",
      "marketSummary": "Adjusted based on your feedback",
      "factors": {
        "scopeWeight": 1.0,
        "complexityWeight": 0.9,
        "experienceWeight": 1.0,
        "urgencyWeight": 1.0,
        "locationWeight": 1.0
      }
    },
    "advancedPreferencesSuggestion": { ... },
    "clarifyingQuestions": [ ... ],
    "warnings": []
  }
}
```

---

## 3️⃣ Ghi Nhận Phản Hồi

**Endpoint**: `POST /feedback/{recommendationId}`

**Full URL**: `http://localhost:8080/api/ai/project-assistant/feedback/1`

**Authorization**: ✅ JWT Token Required

**Headers**:
```
Content-Type: application/json
Authorization: Bearer {YOUR_JWT_TOKEN}
```

**Request Body**:
```json
{
  "feedback": "ACCEPTED",
  "notes": "Great suggestions! Will use most of them."
}
```

**Feedback Types**:
- `"ACCEPTED"` - Chấp nhận tất cả gợi ý
- `"REJECTED"` - Từ chối tất cả
- `"PARTIAL"` - Chấp nhận một phần

**Response (200 OK)**:
```json
{
  "statusCode": "200",
  "statusMessage": "Feedback recorded successfully"
}
```

**Error Response (400)**:
```json
{
  "statusCode": "400",
  "statusMessage": "Invalid feedback type"
}
```

---

## 4️⃣ Lấy Lịch Sử Gợi Ý

**Endpoint**: `GET /history?page=0&size=10`

**Full URL**: `http://localhost:8080/api/ai/project-assistant/history?page=0&size=10`

**Authorization**: ✅ JWT Token Required

**Headers**:
```
Authorization: Bearer {YOUR_JWT_TOKEN}
```

**Query Parameters**:
- `page` (default: 0) - Trang, bắt đầu từ 0
- `size` (default: 10) - Số item per trang

**Response (200 OK)**:
```json
{
  "statusCode": "200",
  "statusMessage": "Recommendation history retrieved",
  "body": [
    {
      "recommendationId": 1,
      "userBrief": "Xây dựng website bán hàng online...",
      "suggestedTitle": "Professional E-Commerce Platform Development",
      "suggestedDescription": "Build a modern e-commerce platform...",
      "suggestedBudgetMin": 3000,
      "suggestedBudgetRecommended": 5000,
      "suggestedBudgetMax": 8000,
      "userFeedback": "ACCEPTED",
      "createdAt": "2026-03-06T10:30:45Z"
    },
    {
      "recommendationId": 2,
      "userBrief": "Build a mobile app",
      "suggestedTitle": "Mobile App Development",
      "suggestedDescription": "...",
      "suggestedBudgetMin": 5000,
      "suggestedBudgetRecommended": 8000,
      "suggestedBudgetMax": 12000,
      "userFeedback": "NOT_PROVIDED",
      "createdAt": "2026-03-06T09:15:30Z"
    }
  ]
}
```

---

## 5️⃣ Lấy Thống Kê Sử Dụng

**Endpoint**: `GET /stats`

**Full URL**: `http://localhost:8080/api/ai/project-assistant/stats`

**Authorization**: ✅ JWT Token Required

**Headers**:
```
Authorization: Bearer {YOUR_JWT_TOKEN}
```

**Response (200 OK)**:
```json
{
  "statusCode": "200",
  "statusMessage": "Usage statistics retrieved",
  "body": {
    "api_calls_24h": 3,
    "estimated_cost_24h": 0.0036,
    "recent_recommendations": 3,
    "accepted_recommendations": 2
  }
}
```

**Giải Thích**:
- `api_calls_24h`: Số lần gọi API trong 24h vừa qua
- `estimated_cost_24h`: Chi phí ước tính (USD)
- `recent_recommendations`: Số gợi ý gần đây
- `accepted_recommendations`: Số gợi ý được chấp nhận

---

## 6️⃣ Kiểm Tra Sức Khỏe

**Endpoint**: `GET /health`

**Full URL**: `http://localhost:8080/api/ai/project-assistant/health`

**Authorization**: ❌ NOT Required

**Response (200 OK)**:
```json
{
  "status": "UP",
  "service": "AI Project Assistant"
}
```

---

## 🔐 Cách Lấy JWT Token

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "your-email@example.com",
    "password": "your-password"
  }'
```

**Response**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": 123,
  "email": "your-email@example.com"
}
```

Lưu token vào `localStorage`:
```javascript
localStorage.setItem('token', response.token);
```

---

## 📋 Enum Values (Lựa Chọn)

### Scope
- `"SMALL"` → 21 ngày (dự án nhỏ)
- `"MEDIUM"` → 60 ngày (dự án trung bình) ⭐
- `"LARGE"` → 120 ngày (dự án lớn)
- `"ENTERPRISE"` → 210 ngày (dự án rất lớn)

### Timeline
- `"Less than 1 month"` → Gấp
- `"1 to 3 months"` → Bình thường ⭐
- `"3 to 6 months"` → Dài hạn
- `"More than 6 months"` → Rất dài hạn

### Experience Level
- `"BEGINNER"` → Người mới bắt đầu
- `"INTERMEDIATE"` → Trung cấp ⭐
- `"EXPERT"` → Chuyên gia

### Complexity Hint
- `"LOW"` → Đơn giản
- `"MEDIUM"` → Trung bình ⭐
- `"HIGH"` → Phức tạp

---

## 🛠️ Copy-Paste Code (JavaScript)

### Fetch API

```javascript
// 1. Suggest
async function suggestProject() {
  const response = await fetch(
    'http://localhost:8080/api/ai/project-assistant/suggest',
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: JSON.stringify({
        brief: 'Xây dựng website bán hàng',
        categoryId: 1,
        scope: 'MEDIUM',
        timeline: '1 to 3 months'
      })
    }
  );
  return await response.json();
}

// 2. Improve
async function improveProject(id, feedback) {
  const response = await fetch(
    `http://localhost:8080/api/ai/project-assistant/improve/${id}`,
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: JSON.stringify({ feedback })
    }
  );
  return await response.json();
}

// 3. Record Feedback
async function recordFeedback(id, feedback) {
  const response = await fetch(
    `http://localhost:8080/api/ai/project-assistant/feedback/${id}`,
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      },
      body: JSON.stringify({ feedback, notes: '' })
    }
  );
  return await response.json();
}

// 4. Get History
async function getHistory() {
  const response = await fetch(
    'http://localhost:8080/api/ai/project-assistant/history?page=0&size=10',
    {
      headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
    }
  );
  return await response.json();
}

// 5. Get Stats
async function getStats() {
  const response = await fetch(
    'http://localhost:8080/api/ai/project-assistant/stats',
    {
      headers: { 'Authorization': `Bearer ${localStorage.getItem('token')}` }
    }
  );
  return await response.json();
}

// 6. Health Check
async function checkHealth() {
  const response = await fetch(
    'http://localhost:8080/api/ai/project-assistant/health'
  );
  return await response.json();
}
```

### Axios

```javascript
import axios from 'axios';

const token = localStorage.getItem('token');

const aiApi = axios.create({
  baseURL: 'http://localhost:8080/api/ai/project-assistant',
  headers: { 'Authorization': `Bearer ${token}` }
});

// 1. Suggest
export const suggestProject = (data) => aiApi.post('/suggest', data);

// 2. Improve
export const improveProject = (id, feedback) => 
  aiApi.post(`/improve/${id}`, { feedback });

// 3. Record Feedback
export const recordFeedback = (id, feedback, notes) =>
  aiApi.post(`/feedback/${id}`, { feedback, notes });

// 4. Get History
export const getHistory = (page = 0, size = 10) =>
  aiApi.get(`/history?page=${page}&size=${size}`);

// 5. Get Stats
export const getStats = () => aiApi.get('/stats');

// 6. Health Check (no auth)
export const checkHealth = () =>
  axios.get('http://localhost:8080/api/ai/project-assistant/health');
```

---

## ⚠️ Error Codes & Status

| Status | Meaning | Example |
|--------|---------|---------|
| **200** | Success | Request thành công |
| **400** | Bad Request | Dữ liệu không hợp lệ, rate limit exceeded |
| **401** | Unauthorized | JWT token hết hạn hoặc không được gửi |
| **500** | Server Error | Server error |

**Common Errors**:
- `"Rate limit exceeded"` → Chờ 1 giờ (max 10 requests/hour)
- `"User not authenticated"` → Token không được gửi hoặc hết hạn
- `"Brief must be between 10 and 2000 characters"` → Brief quá ngắn

---

## 📞 Testing with cURL

```bash
# 1. Health Check
curl http://localhost:8080/api/ai/project-assistant/health

# 2. Suggest (replace {token} with your JWT)
curl -X POST http://localhost:8080/api/ai/project-assistant/suggest \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "brief": "Build e-commerce website",
    "categoryId": 1,
    "scope": "MEDIUM",
    "timeline": "1 to 3 months"
  }'

# 3. Improve
curl -X POST http://localhost:8080/api/ai/project-assistant/improve/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{"feedback": "Budget too high"}'

# 4. Feedback
curl -X POST http://localhost:8080/api/ai/project-assistant/feedback/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{"feedback": "ACCEPTED"}'

# 5. History
curl http://localhost:8080/api/ai/project-assistant/history?page=0&size=10 \
  -H "Authorization: Bearer {token}"

# 6. Stats
curl http://localhost:8080/api/ai/project-assistant/stats \
  -H "Authorization: Bearer {token}"
```

---

## ✅ Summary

| # | Endpoint | Method | Auth | Purpose |
|----|----------|--------|------|---------|
| 1 | `/suggest` | POST | ✅ | Tạo gợi ý dự án |
| 2 | `/improve/{id}` | POST | ✅ | Cải thiện gợi ý |
| 3 | `/feedback/{id}` | POST | ✅ | Ghi nhận phản hồi |
| 4 | `/history` | GET | ✅ | Lịch sử gợi ý |
| 5 | `/stats` | GET | ✅ | Thống kê sử dụng |
| 6 | `/health` | GET | ❌ | Kiểm tra sức khỏe |

---

**Ready! Copy endpoints & implement trong frontend! 🚀**

