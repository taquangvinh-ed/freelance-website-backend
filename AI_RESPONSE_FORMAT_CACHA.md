# AI Project Assistant - Response Format (Cách B)

## 📤 Response Schema

Endpoint `POST /api/ai/project-assistant/suggest` trả về **format clean (không wrapper ResponseDTO)**:

```json
{
  "success": true,
  "message": "AI suggestion generated successfully",
  "timestamp": "2026-03-06T10:30:00Z",
  "body": {
    "requestId": "ai_req_01HXYZ...",
    "projectDraft": {
      "title": "string",
      "description": "string",
      "categoryId": 0,
      "scope": "SMALL|MEDIUM|LARGE|ENTERPRISE",
      "skills": ["React", "Node.js", "PostgreSQL"],
      "timelineDays": 30
    },
    "budgetSuggestion": {
      "currency": "USD",
      "min": 800,
      "recommended": 1200,
      "max": 1600,
      "confidence": 0.82,
      "pricingSource": "market_stats_v1",
      "marketSummary": "Dựa trên 245 dự án tương tự.",
      "factors": {
        "scopeWeight": 1.0,
        "complexityWeight": 1.1,
        "experienceWeight": 1.0,
        "urgencyWeight": 1.0,
        "locationWeight": 1.0
      }
    },
    "advancedPreferencesSuggestion": {
      "experienceLevel": "INTERMEDIATE",
      "projectDuration": 30,
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
      "Bạn có cần tích hợp cổng thanh toán không?",
      "Bạn cần bao nhiêu người quản lý backend?"
    ],
    "warnings": []
  }
}
```

---

## 🔍 Chi Tiết Từng Field

### Top Level

| Field | Type | Mô Tả |
|-------|------|-------|
| `success` | boolean | `true` nếu request thành công |
| `message` | string | Thông báo từ server |
| `timestamp` | string (ISO-8601) | Thời gian response được tạo |
| `body` | object | Dữ liệu chính của response |

### `body.projectDraft`

| Field | Type | Mô Tả |
|-------|------|-------|
| `title` | string | Tiêu đề dự án gợi ý |
| `description` | string | Mô tả chi tiết dự án |
| `categoryId` | number | ID của category |
| `scope` | string | Quy mô: SMALL, MEDIUM, LARGE, ENTERPRISE |
| `skills` | array[string] | Danh sách kỹ năng cần thiết |
| `timelineDays` | number | Thời gian dự án (ngày) |

### `body.budgetSuggestion`

| Field | Type | Mô Tả |
|-------|------|-------|
| `currency` | string | Loại tiền tệ (USD) |
| `min` | number | Giá tối thiểu |
| `recommended` | number | Giá đề xuất ⭐ |
| `max` | number | Giá tối đa |
| `confidence` | number | Độ tin cậy (0.0 - 1.0) |
| `pricingSource` | string | Nguồn dữ liệu ("market_stats_v1") |
| `marketSummary` | string | Mô tả thị trường |
| `factors` | object | Chi tiết các hệ số điều chỉnh |

### `body.budgetSuggestion.factors`

| Field | Type | Mô Tả |
|-------|------|-------|
| `scopeWeight` | number | Hệ số quy mô |
| `complexityWeight` | number | Hệ số độ phức tạp (0.9-1.1) |
| `experienceWeight` | number | Hệ số kinh nghiệm |
| `urgencyWeight` | number | Hệ số thời gian (1.0 hoặc 1.1 nếu gấp) |
| `locationWeight` | number | Hệ số vị trí |

### `body.advancedPreferencesSuggestion`

| Field | Type | Mô Tả |
|-------|------|-------|
| `experienceLevel` | string | BEGINNER, INTERMEDIATE, EXPERT |
| `projectDuration` | number | Thời gian dự án (ngày) |
| `hoursPerWeek` | number | Giờ làm việc/tuần |
| `freelancerType` | string | INDIVIDUAL, TEAM |
| `locationPreference` | string | ANY, SAME_COUNTRY, SAME_TIMEZONE |
| `timezoneOverlap` | string | Khoảng thời gian trùng lặp |
| `englishLevel` | string | BASIC, CONVERSATIONAL, FLUENT |
| `projectType` | string | ONE_TIME, ONGOING, RETAINER |
| `complexity` | string | LOW, MEDIUM, HIGH |
| `urgent` | boolean | Dự án có gấp không? |

### `body.clarifyingQuestions`

Array các câu hỏi mà client nên trả lời để làm rõ yêu cầu dự án.

Ví dụ:
```json
[
  "Bạn có cần tích hợp cổng thanh toán không?",
  "Cần hỗ trợ nhiều ngôn ngữ không?",
  "Bạn có data cũ cần migrate không?"
]
```

### `body.warnings`

Array các cảnh báo (hiện tại để trống). Có thể dùng để thêm các lưu ý tương lai.

---

## ✅ Ví Dụ Response Đầy Đủ

```json
{
  "success": true,
  "message": "AI suggestion generated successfully",
  "timestamp": "2026-03-06T10:30:45Z",
  "body": {
    "requestId": "ai_req_a1b2c3d4e5f6g7h8i9j0",
    "projectDraft": {
      "title": "Professional E-Commerce Platform Development",
      "description": "Build a modern e-commerce platform with React frontend, Node.js backend, PostgreSQL database. Must support product catalog, shopping cart, payment integration with Stripe, user authentication, order management. Expected to handle 1000+ concurrent users.",
      "categoryId": 1,
      "scope": "LARGE",
      "skills": [
        "React",
        "Node.js",
        "PostgreSQL",
        "Stripe API",
        "Docker",
        "AWS",
        "REST API Design"
      ],
      "timelineDays": 120
    },
    "budgetSuggestion": {
      "currency": "USD",
      "min": 5000,
      "recommended": 12000,
      "max": 25000,
      "confidence": 0.87,
      "pricingSource": "market_stats_v1",
      "marketSummary": "Dựa trên 245 dự án tương tự trong Web Development category.",
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
      "hoursPerWeek": 40,
      "freelancerType": "TEAM",
      "locationPreference": "ANY",
      "timezoneOverlap": "4-8h",
      "englishLevel": "FLUENT",
      "projectType": "ONE_TIME",
      "complexity": "HIGH",
      "urgent": false
    },
    "clarifyingQuestions": [
      "Bạn muốn sử dụng cổng thanh toán nào? (Stripe, PayPal, Momo)",
      "Cần hỗ trợ nhiều tiền tệ không?",
      "Bạn có dữ liệu sản phẩm cũ cần migrate không?",
      "Cần analytics/reporting dashboard không?"
    ],
    "warnings": []
  }
}
```

---

## ❌ Error Response

Khi có lỗi, vẫn trả format tương tự nhưng `success: false`:

```json
{
  "success": false,
  "message": "Rate limit exceeded. Maximum 10 requests per hour.",
  "timestamp": "2026-03-06T10:31:00Z",
  "body": null
}
```

Hoặc:

```json
{
  "success": false,
  "message": "User not authenticated",
  "timestamp": "2026-03-06T10:31:00Z",
  "body": null
}
```

---

## 🔄 Cách Sử Dụng Frontend (React)

### Request

```javascript
const response = await fetch(
  'http://localhost:8080/api/ai/project-assistant/suggest',
  {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify({
      brief: "Xây dựng website bán hàng online",
      categoryId: 1,
      scope: "MEDIUM",
      timeline: "1 to 3 months",
      experienceLevel: "INTERMEDIATE"
    })
  }
);

const data = await response.json();
```

### Parse Response

```javascript
if (data.success) {
  // ✅ Success
  const projectDraft = data.body.projectDraft;
  const budget = data.body.budgetSuggestion;
  
  console.log("Title:", projectDraft.title);
  console.log("Budget:", budget.recommended);
  console.log("Skills:", projectDraft.skills);
  console.log("Confidence:", budget.confidence);
  console.log("Questions:", data.body.clarifyingQuestions);
} else {
  // ❌ Error
  console.error("Error:", data.message);
}
```

---

## 📋 TypeScript Interface

```typescript
interface AIProjectAssistantResponse {
  success: boolean;
  message: string;
  timestamp: string;
  body: {
    requestId: string;
    projectDraft: {
      title: string;
      description: string;
      categoryId: number;
      scope: "SMALL" | "MEDIUM" | "LARGE" | "ENTERPRISE";
      skills: string[];
      timelineDays: number;
    };
    budgetSuggestion: {
      currency: string;
      min: number;
      recommended: number;
      max: number;
      confidence: number;
      pricingSource: string;
      marketSummary: string;
      factors: {
        scopeWeight: number;
        complexityWeight: number;
        experienceWeight: number;
        urgencyWeight: number;
        locationWeight: number;
      };
    };
    advancedPreferencesSuggestion: {
      experienceLevel: "BEGINNER" | "INTERMEDIATE" | "EXPERT";
      projectDuration: number;
      hoursPerWeek: number;
      freelancerType: "INDIVIDUAL" | "TEAM";
      locationPreference: string;
      timezoneOverlap: string;
      englishLevel: string;
      projectType: "ONE_TIME" | "ONGOING" | "RETAINER";
      complexity: "LOW" | "MEDIUM" | "HIGH";
      urgent: boolean;
    };
    clarifyingQuestions: string[];
    warnings: string[];
  } | null;
}
```

---

## 🚀 Tóm Tắt

| Aspect | Chi Tiết |
|--------|----------|
| **Response Type** | JSON (không wrapper ResponseDTO) |
| **Success Status** | `success: true` |
| **Error Status** | `success: false` |
| **HTTP Status** | 200 (OK), 400 (Bad Request), 401 (Unauthorized), 500 (Server Error) |
| **Timestamp Format** | ISO-8601 (e.g., "2026-03-06T10:30:45Z") |
| **RequestId** | Unique ID để track request |
| **Main Data** | Trong `body` object |
| **Budget Factors** | Giải thích tại sao giá được tính như vậy |
| **Clarifying Questions** | Giúp client làm rõ yêu cầu |

---

## ✨ Lợi Ích của Cách B

✅ Frontend nhận đúng schema mong muốn
✅ Không lồng ghép `body` nhiều lần
✅ Sạch sẽ, dễ parse
✅ TypeScript interface rõ ràng
✅ Phù hợp với design pattern của frontend team


