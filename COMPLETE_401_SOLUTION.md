# 🎯 COMPLETE 401 ERROR SOLUTION

## 🔍 Problem Summary

Frontend shows:
- ✅ Token found: true
- ✅ Token exists in localStorage
- ❌ Backend returns 401 Unauthorized
- ❌ Error: "Tên đăng nhập hoặc mật khẩu không đúng"

## 🐛 Root Cause

**Token is EXPIRED or INVALID**

JWT tokens in your backend expire after **16.8 hours** (60480000ms).

## ✅ IMMEDIATE FIX

### Option 1: Re-Login (Quickest)

**In Browser Console:**
```javascript
// 1. Clear old token
localStorage.removeItem('token');

// 2. Go to login page
window.location.href = '/login';

// 3. Login again with your credentials
// 4. New token will be saved automatically
// 5. Try AI suggestion again
```

### Option 2: Check Token Expiry

**In Browser Console:**
```javascript
const token = localStorage.getItem('token');
if (token) {
  try {
    const parts = token.split('.');
    const payload = JSON.parse(atob(parts[1]));
    
    const issued = new Date(payload.iat * 1000);
    const expires = new Date(payload.exp * 1000);
    const now = new Date();
    
    console.log('📅 Token issued at:', issued.toLocaleString());
    console.log('⏰ Token expires at:', expires.toLocaleString());
    console.log('🕐 Current time:', now.toLocaleString());
    console.log('❌ Expired?', now > expires ? 'YES - RE-LOGIN NEEDED!' : 'NO');
    
    if (now > expires) {
      console.log('🔄 Token expired. Clearing...');
      localStorage.removeItem('token');
      console.log('✅ Please login again');
    }
  } catch (e) {
    console.error('Token decode error:', e);
    localStorage.removeItem('token');
    console.log('✅ Invalid token removed. Please login again');
  }
}
```

---

## 🔧 PERMANENT FIX (Frontend Code)

### Update `api.ts` with Token Expiry Check

Add this to your frontend API configuration:

```typescript
// src/services/api.ts

import axios, { AxiosError } from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

// Request interceptor - Check token before EVERY request
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    
    if (token) {
      try {
        // Decode JWT token
        const parts = token.split('.');
        if (parts.length !== 3) {
          throw new Error('Invalid token format');
        }
        
        const payload = JSON.parse(atob(parts[1]));
        const expiry = payload.exp * 1000; // Convert to milliseconds
        const now = Date.now();
        
        // Check if token expired
        if (now >= expiry) {
          console.warn('⏰ Token expired. Redirecting to login...');
          localStorage.removeItem('token');
          window.location.href = '/login';
          return Promise.reject(new Error('Token expired'));
        }
        
        // Check if token expires soon (within 1 hour)
        const oneHour = 60 * 60 * 1000;
        if (expiry - now < oneHour) {
          console.warn('⚠️ Token expires soon. Consider refreshing...');
        }
        
        // Add token to header
        config.headers.Authorization = `Bearer ${token}`;
        console.log('🔑 Token attached to request');
        
      } catch (error) {
        console.error('❌ Token decode error:', error);
        localStorage.removeItem('token');
        window.location.href = '/login';
        return Promise.reject(new Error('Invalid token'));
      }
    } else {
      console.warn('⚠️ No token found');
    }
    
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor - Handle 401 errors
api.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    if (error.response?.status === 401) {
      console.error('❌ 401 Unauthorized - Token invalid or expired');
      
      // Clear token
      localStorage.removeItem('token');
      
      // Show user-friendly message
      alert('Your session has expired. Please login again.');
      
      // Redirect to login
      window.location.href = '/login';
    }
    
    return Promise.reject(error);
  }
);

export default api;

// AI Project Assistant API
export const aiProjectAssistant = {
  suggest: (data: any) => api.post('/ai/project-assistant/suggest', data),
  improve: (id: number, feedback: string) => 
    api.post(`/ai/project-assistant/improve/${id}`, { feedback }),
  feedback: (id: number, feedbackData: any) => 
    api.post(`/ai/project-assistant/feedback/${id}`, feedbackData),
  history: (page = 0, size = 10) => 
    api.get(`/ai/project-assistant/history?page=${page}&size=${size}`),
  stats: () => api.get('/ai/project-assistant/stats'),
  health: () => api.get('/ai/project-assistant/health')
};
```

---

## 🧪 TEST STEPS

### 1. Clear Everything and Start Fresh

```javascript
// In browser console
localStorage.clear();
console.log('✅ All tokens cleared');
```

### 2. Login

Navigate to login page and login with valid credentials.

Check token is saved:
```javascript
const token = localStorage.getItem('token');
console.log('Token saved:', token ? 'YES ✅' : 'NO ❌');
```

### 3. Test AI Endpoint

Call `/api/ai/project-assistant/suggest` from your component.

### 4. Check Logs

**Frontend console should show:**
```
🔑 Token attached to request
✅ Request successful
```

**Backend logs should show:**
```
🔐 JWT Filter processing: /api/ai/project-assistant/suggest
🔑 JWT extracted: YES (length: 250)
🔍 Token validation result: true
✅ JWT token is valid
📧 Email from JWT: user@example.com
👤 User loaded: user@example.com
✅ Authentication set in SecurityContext
Suggest endpoint called, appUser: user@example.com
```

---

## 📊 Token Lifecycle Explained

```
User Login
    ↓
Backend generates JWT token
    - Issued at (iat): March 6, 2026 09:00
    - Expires at (exp): March 6, 2026 + 16.8 hours = March 7, 2026 01:48
    ↓
Token saved in localStorage
    ↓
User makes requests (JWT in Authorization header)
    ↓
[Time passes... 16.8 hours]
    ↓
Token EXPIRES
    ↓
Next request → 401 Unauthorized
    ↓
Frontend detects 401 → Clear token → Redirect to login
```

---

## 🔍 Backend Logs You Should See

After I added logging, your backend should show:

### Success Case
```
🔐 JWT Filter processing: /api/ai/project-assistant/suggest
🔑 JWT extracted: YES (length: 250)
🔍 Token validation result: true
✅ JWT token is valid
📧 Email from JWT: user@example.com
👤 User loaded: user@example.com
✅ Authentication set in SecurityContext for user: user@example.com
[INFO] Suggest endpoint called, appUser: user@example.com
[INFO] Generating AI suggestions for user=123
```

### Token Expired
```
🔐 JWT Filter processing: /api/ai/project-assistant/suggest
🔑 JWT extracted: YES (length: 250)
🔍 Token validation result: false
❌ Expired JWT Token: JWT expired at 2026-03-06T09:00:00Z. Current time: ...
[WARN] Suggest endpoint called, appUser: NULL
[WARN] AppUser is null - authentication failed
```

### No Token
```
🔐 JWT Filter processing: /api/ai/project-assistant/suggest
🔑 JWT extracted: NO
❌ No JWT token found in request to: /api/ai/project-assistant/suggest
[WARN] Suggest endpoint called, appUser: NULL
[WARN] AppUser is null - authentication failed
```

---

## 🚨 Common Mistakes to Avoid

### ❌ Mistake 1: Not Adding "Bearer " Prefix
```javascript
// WRONG
headers: { 'Authorization': token }

// CORRECT
headers: { 'Authorization': `Bearer ${token}` }
```

### ❌ Mistake 2: Using Expired Token
```javascript
// Check expiry BEFORE sending
const payload = JSON.parse(atob(token.split('.')[1]));
if (Date.now() > payload.exp * 1000) {
  // Token expired - don't use it!
  localStorage.removeItem('token');
  // Redirect to login
}
```

### ❌ Mistake 3: Not Handling 401 Globally
```javascript
// Add interceptor to handle ALL 401 errors
axios.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);
```

---

## 📱 Quick Commands

### Check Token in Browser Console
```javascript
const token = localStorage.getItem('token');
const payload = JSON.parse(atob(token.split('.')[1]));
console.table({
  'Email': payload.sub,
  'User ID': payload.userId,
  'Role': payload.role,
  'Issued': new Date(payload.iat * 1000).toLocaleString(),
  'Expires': new Date(payload.exp * 1000).toLocaleString(),
  'Valid': Date.now() < payload.exp * 1000 ? '✅ YES' : '❌ NO'
});
```

### Force Logout and Clear
```javascript
localStorage.clear();
sessionStorage.clear();
window.location.href = '/login';
```

---

## ✅ CHECKLIST

- [ ] Clear old token: `localStorage.removeItem('token')`
- [ ] Login again with valid credentials
- [ ] Verify token saved: `localStorage.getItem('token')`
- [ ] Check token not expired (use code above)
- [ ] Test AI endpoint again
- [ ] Check backend logs for emoji indicators
- [ ] If still fails, check backend is running

---

## 🎯 90% Solution

**Most likely your token just expired. Do this:**

```javascript
// 1. Clear
localStorage.clear();

// 2. Login again
// Go to /login page and login

// 3. Done!
```

---

## 📚 Additional Resources

- `ENHANCED_JWT_LOGGING_COMPLETE.md` - Backend logging details
- `DEBUG_401_WITH_LOGS.md` - Step-by-step debugging
- `ERROR_401_TROUBLESHOOTING.md` - Troubleshooting guide

---

**Summary: Token expired → Re-login → Problem solved! 🎉**

