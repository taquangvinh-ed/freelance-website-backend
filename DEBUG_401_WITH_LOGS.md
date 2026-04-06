# 🔍 DEBUG 401 ERROR - Step by Step

## ✅ You've Added Logs - Now Check Them!

### 1. Restart Backend Server
```bash
# Stop current server (Ctrl+C)
# Start again
./gradlew bootRun
```

### 2. Make Request from Frontend
Frontend should call: `POST http://localhost:8080/api/ai/project-assistant/suggest`

### 3. Check Backend Logs

You should see logs like this:

#### ✅ Success Case
```
🔐 JWT Filter processing: /api/ai/project-assistant/suggest
🔑 JWT extracted: YES (length: 250)
🔍 Token validation result: true
✅ JWT token is valid
📧 Email from JWT: user@example.com
👤 User loaded: user@example.com
✅ Authentication set in SecurityContext for user: user@example.com
Suggest endpoint called, appUser: user@example.com
```

#### ❌ Failure Case (Token Invalid)
```
🔐 JWT Filter processing: /api/ai/project-assistant/suggest
🔑 JWT extracted: YES (length: 250)
🔍 Token validation result: false
❌ Expired JWT Token: JWT expired at 2026-03-05T10:00:00Z
Suggest endpoint called, appUser: NULL
```

#### ❌ Failure Case (No Token)
```
🔐 JWT Filter processing: /api/ai/project-assistant/suggest
🔑 JWT extracted: NO
❌ No JWT token found in request to: /api/ai/project-assistant/suggest
Suggest endpoint called, appUser: NULL
```

---

## 🔍 Based on Logs, Take Action

### Case 1: "JWT extracted: NO"
**Problem**: Token not being sent from frontend

**Fix in Frontend**:
```javascript
// Check if Authorization header is set
console.log('Authorization header:', config.headers.Authorization);

// Should be: "Bearer eyJhbGci..."
```

### Case 2: "❌ Expired JWT Token"
**Problem**: Token expired (JWT_EXPIRATION = 60480000ms = ~16 hours)

**Fix**: Login again to get new token
```javascript
// Frontend: Re-login
const response = await api.post('/login', {
  email: 'user@example.com',
  password: 'password'
});

// Save new token
localStorage.setItem('token', response.data.body);
```

### Case 3: "❌ Invalid JWT Token"
**Problem**: Token corrupted or signature mismatch

**Fix**: Clear token and re-login
```javascript
localStorage.removeItem('token');
// Redirect to login page
```

### Case 4: "👤 User loaded: NULL"
**Problem**: Email in JWT doesn't exist in database

**Fix**: Check if user account exists
```sql
SELECT * FROM users WHERE email = 'user@example.com';
```

---

## 🧪 Quick Test Commands

### Test 1: Get Fresh Token
```bash
# Login to get new token
TOKEN=$(curl -s -X POST http://localhost:8080/api/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password"}' \
  | jq -r '.body')

echo "Token: $TOKEN"
```

### Test 2: Test Endpoint with Token
```bash
curl -v -X POST http://localhost:8080/api/ai/project-assistant/suggest \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "brief": "Build website",
    "categoryId": 1,
    "scope": "MEDIUM",
    "timeline": "1 to 3 months"
  }'
```

### Test 3: Decode Token (Check Expiry)
```bash
# Decode JWT token (paste your token)
TOKEN="your-token-here"

# Extract payload (base64 decode)
echo $TOKEN | cut -d'.' -f2 | base64 -d 2>/dev/null | jq .

# Should show:
# {
#   "role": "CLIENT",
#   "userId": 1,
#   "sub": "user@example.com",
#   "iat": 1741132800,  # Issued at
#   "exp": 1741193280   # Expires at
# }
```

---

## 📊 Debugging Checklist

- [ ] Backend logs show "🔑 JWT extracted: YES"
- [ ] Backend logs show "✅ JWT token is valid"
- [ ] Backend logs show "👤 User loaded: {email}"
- [ ] Backend logs show "✅ Authentication set"
- [ ] Controller logs show "appUser: {username}" (not NULL)

If ALL checked ✅ → Request should succeed!

---

## 🚨 Common Issues & Solutions

### Issue: "Token exists: true" but "JWT extracted: NO"
**Cause**: Frontend sends token but backend doesn't receive it

**Check**:
1. CORS configuration allows Authorization header
2. Browser/proxy doesn't strip header
3. Request goes to correct URL

### Issue: Token validation fails immediately
**Cause**: 
- JWT_SECRET mismatch
- Token from different environment
- Token format corrupted

**Fix**: Get fresh token from current backend

### Issue: User loaded: NULL
**Cause**: Email in JWT doesn't exist in database

**Fix**: 
```sql
-- Check if user exists
SELECT * FROM users WHERE email = 'email-from-jwt';

-- If not exists, create or use existing user's email
```

---

## 🎯 NEXT STEPS

1. **Restart backend** with logging enabled
2. **Make request from frontend**
3. **Check backend logs** (terminal where Spring Boot runs)
4. **Find which case** matches your logs
5. **Apply fix** from above

---

## 📱 Contact Logs Location

Logs appear in terminal where you run:
```bash
./gradlew bootRun
```

Look for emoji icons: 🔐 🔑 ✅ ❌ 📧 👤

---

**Once you see logs, you'll know EXACTLY what's wrong!** 🎯

