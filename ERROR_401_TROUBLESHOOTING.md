# 🐛 ERROR 401 (Unauthorized) - TROUBLESHOOTING GUIDE

## ❌ Error

```
POST http://localhost:8080/api/ai/project-assistant/suggest
Status: 401 (Unauthorized)
Message: "User not authenticated"
```

## 🔍 Root Cause

**Frontend không gửi JWT token hoặc token không hợp lệ**

## ✅ Solutions

### 1. Check Token in Frontend

Verify token is being sent:

```javascript
// Check if token exists
const token = localStorage.getItem('token');
console.log('Token:', token);

// Check request headers
fetch('http://localhost:8080/api/ai/project-assistant/suggest', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${token}`,  // ← MUST have "Bearer " prefix
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({...})
})
```

### 2. Get New Token (Login)

```bash
# Login to get fresh token
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "password": "password"
  }'

# Response
{
  "statusCode": "200",
  "statusMessage": "Request successful",
  "body": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."  # ← This is the token
}
```

### 3. Test Token with curl

```bash
TOKEN="your-jwt-token-here"

curl -X POST http://localhost:8080/api/ai/project-assistant/suggest \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "brief": "Build website",
    "categoryId": 1,
    "scope": "MEDIUM",
    "timeline": "1 to 3 months"
  }'
```

### 4. Common Mistakes

#### ❌ Missing "Bearer " prefix
```javascript
// WRONG
headers: {
  'Authorization': token  // Missing "Bearer "
}

// CORRECT
headers: {
  'Authorization': `Bearer ${token}`
}
```

#### ❌ Token expired
```javascript
// Check token expiration
const tokenParts = token.split('.');
const payload = JSON.parse(atob(tokenParts[1]));
const expiry = payload.exp * 1000;
const now = Date.now();

if (now > expiry) {
  console.log('Token expired, please login again');
  // Redirect to login
}
```

#### ❌ Token not stored
```javascript
// After login, MUST store token
const response = await fetch('http://localhost:8080/api/login', {...});
const data = await response.json();

localStorage.setItem('token', data.body);  // ← MUST save
```

## 🔧 Frontend Code Examples

### React/Vue/Angular

```javascript
// Create axios instance with interceptor
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api'
});

// Add token to all requests
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Use it
const suggestProject = async (data) => {
  try {
    const response = await api.post('/ai/project-assistant/suggest', data);
    return response.data;
  } catch (error) {
    if (error.response?.status === 401) {
      // Token invalid - redirect to login
      window.location.href = '/login';
    }
    throw error;
  }
};
```

### Fetch API

```javascript
const suggestProject = async (data) => {
  const token = localStorage.getItem('token');
  
  if (!token) {
    console.error('No token found - please login');
    window.location.href = '/login';
    return;
  }

  const response = await fetch('http://localhost:8080/api/ai/project-assistant/suggest', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(data)
  });

  if (response.status === 401) {
    console.error('Token expired or invalid');
    localStorage.removeItem('token');
    window.location.href = '/login';
    return;
  }

  return await response.json();
};
```

## 🧪 Testing Steps

### Step 1: Login & Get Token
```bash
curl -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password"}' \
  | jq -r '.body'
```

### Step 2: Save Token
```javascript
// In browser console
localStorage.setItem('token', 'YOUR_TOKEN_HERE');
```

### Step 3: Test Endpoint
```bash
curl -X POST http://localhost:8080/api/ai/project-assistant/suggest \
  -H "Authorization: Bearer YOUR_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "brief": "Build e-commerce website",
    "categoryId": 1,
    "scope": "MEDIUM",
    "timeline": "1 to 3 months"
  }'
```

### Expected Response (Success)
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
      "skills": ["React", "Node.js"],
      "timelineDays": 60
    },
    "budgetSuggestion": {
      "min": 3000,
      "recommended": 5000,
      "max": 8000
    }
  }
}
```

## 📊 Security Flow

```
Frontend Request
    ↓
    (Has "Authorization: Bearer {token}" header?)
    ↓
YES → JwtAuthenticationFilter
    ↓
    Token valid?
    ↓
YES → Set SecurityContext with AppUser
    ↓
Controller receives @AuthenticationPrincipal AppUser
    ↓
appUser != null ✅
    ↓
Process request

NO → appUser == null ❌
    ↓
Return 401 Unauthorized
```

## ✅ Checklist

- [ ] Token exists in localStorage
- [ ] Token format: "Bearer {jwt}"
- [ ] Token not expired
- [ ] Request has Authorization header
- [ ] Backend logs show "AppUser is null" (check server logs)
- [ ] Try with fresh token from /api/login

## 🔍 Debug Backend

Check server logs for:

```
[WARN] AppUser is null - authentication failed
[ERROR] Invalid Jwt Token
[ERROR] Expired Jwt Token
```

If you see these, token is invalid/expired.

## 📝 Summary

**Problem**: 401 Unauthorized
**Cause**: No JWT token or invalid token
**Solution**: 
1. Login to get new token
2. Store token: `localStorage.setItem('token', token)`
3. Send with request: `Authorization: Bearer {token}`
4. Check token expiry before sending

---

**Once token is sent correctly, endpoint will work! 🚀**

