# AI Project Assistant - Getting Started Checklist

## 🚀 Phase 0: Pre-Implementation (30 minutes)

### 1. Get Claude API Key
- [ ] Go to https://console.anthropic.com/account/keys
- [ ] Click "Create Key"
- [ ] Copy the API key (starts with `sk-ant-`)
- [ ] Store securely in `.env` file:
  ```bash
  AI_LLM_API_KEY=sk-ant-your-key-here
  ```

### 2. Prepare Environment
```bash
# Set environment variable
export AI_LLM_API_KEY="sk-ant-your-key-here"

# Or add to .env file
echo "AI_LLM_API_KEY=sk-ant-your-key-here" >> .env
```

### 3. Verify Build
```bash
./gradlew clean build
```

---

## 📦 Phase 1: Database Setup (15 minutes)

### 1. Run Migration
```bash
# Option A: Using psql
psql -U postgres -d freelancer_db -f database_migration_ai_project_assistant.sql

# Option B: If using Docker
docker exec freelancer-db psql -U postgres -d freelancer_db -f database_migration_ai_project_assistant.sql

# Option C: Let Spring handle it (if ddl-auto: update)
# Just start the application, tables will be created automatically
```

### 2. Verify Tables Created
```sql
-- Connect to database
psql -U postgres -d freelancer_db

-- Check tables exist
\dt ai_*

-- Should see:
-- ai_api_logs
-- ai_project_recommendations
-- market_price_stats

-- Verify views
\dv

-- Should see:
-- ai_usage_stats
-- ai_recommendation_analytics
-- market_price_insights
```

---

## 🔧 Phase 2: Application Setup (20 minutes)

### 1. Verify Configuration
Check `src/main/resources/application-dev.yml`:
```yaml
ai:
  llm:
    model: claude-3-5-haiku-20241022
    api-key: ${AI_LLM_API_KEY}
  rate-limit:
    requests-per-hour: 10
```

### 2. Build Application
```bash
./gradlew clean build

# Should complete successfully with no errors
# Some warnings about unused imports are OK
```

### 3. Start Application
```bash
./gradlew bootRun

# Or if using IDE, run: FreelancerBackendApplication.java
```

### 4. Verify Server Running
```bash
curl http://localhost:8080/api/ai/project-assistant/health

# Expected response:
# {"status":"UP","service":"AI Project Assistant"}
```

---

## 🧪 Phase 3: Testing (20 minutes)

### 1. Get JWT Token
First, you need to authenticate as a user:

```bash
# Login to get JWT token
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "client@example.com",
    "password": "password"
  }'

# Save the token from response
export JWT_TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### 2. Test Generate Suggestions
```bash
curl -X POST http://localhost:8080/api/ai/project-assistant/suggest \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "brief": "I need to build an e-commerce website with payment integration and user authentication. Should support at least 1000 concurrent users.",
    "categoryId": 1,
    "scope": "MEDIUM",
    "timeline": "1 to 3 months",
    "experienceLevel": "INTERMEDIATE",
    "preferredSkills": ["React", "Node.js"]
  }' | jq .
```

Expected response structure:
```json
{
  "statusCode": "200",
  "statusMessage": "Project suggestions generated successfully",
  "body": {
    "suggestedTitle": "Professional Web Development for E-commerce Platform",
    "suggestedDescription": "...",
    "suggestedSkills": [...],
    "budgetSuggestion": {...},
    "suggestedMilestones": [...],
    "clarifyingQuestions": [...],
    "overallConfidence": 0.82,
    "reasoning": "..."
  }
}
```

### 3. Test Get Stats
```bash
curl http://localhost:8080/api/ai/project-assistant/stats \
  -H "Authorization: Bearer $JWT_TOKEN" | jq .

# Expected:
# {
#   "api_calls_24h": 1,
#   "estimated_cost_24h": "0.0012",
#   "recent_recommendations": 1,
#   "accepted_recommendations": 0
# }
```

### 4. Test Record Feedback
```bash
# First need the recommendationId from previous response
# Let's assume it's 1

curl -X POST http://localhost:8080/api/ai/project-assistant/feedback/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -d '{
    "feedback": "ACCEPTED",
    "notes": "Great suggestions! Will use most of them."
  }'
```

### 5. Check Database
```bash
# View recommendations created
psql -U postgres -d freelancer_db -c "SELECT * FROM ai_project_recommendations LIMIT 5;"

# View API logs
psql -U postgres -d freelancer_db -c "SELECT * FROM ai_api_logs LIMIT 5;"

# View market stats
psql -U postgres -d freelancer_db -c "SELECT * FROM market_price_stats LIMIT 5;"

# View usage stats
psql -U postgres -d freelancer_db -c "SELECT * FROM ai_usage_stats;"
```

---

## 📱 Phase 4: Frontend Integration (30 minutes)

### 1. Copy Component
Copy the React component from `AI_PROJECT_ASSISTANT_FRONTEND.md` to your frontend:
- `ProjectAssistantModal.jsx`
- `ProjectAssistantStyles.css`

### 2. Import Component
In your project creation page:
```jsx
import ProjectAssistantModal from './components/ProjectAssistantModal';

export default function CreateProject() {
  const [projectData, setProjectData] = useState(null);
  
  return (
    <div>
      <ProjectAssistantModal 
        onProjectGenerated={(data) => setProjectData(data)}
      />
    </div>
  );
}
```

### 3. Update API Base URL
In your API client (e.g., `src/api/client.js`):
```javascript
export const apiClient = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${localStorage.getItem('token')}`
  }
});
```

### 4. Test UI
1. Navigate to project creation page
2. Click "Generate with AI" button
3. Fill in brief and category
4. See AI suggestions appear
5. Accept suggestions
6. Project is created

---

## ✅ Verification Checklist

- [ ] API key is set in environment
- [ ] Database tables created successfully
- [ ] Application starts without errors
- [ ] Health endpoint returns 200
- [ ] Can authenticate and get JWT token
- [ ] Generate suggestions endpoint works
- [ ] Recommendations appear in database
- [ ] Feedback can be recorded
- [ ] Rate limiting works (try 11 requests in 1 hour)
- [ ] React component renders
- [ ] Frontend can call backend API
- [ ] Suggestions display correctly

---

## 🐛 Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| `Flyway migration error` | Tables may already exist. Check with `\dt ai_*` in psql |
| `API key invalid` | Verify key starts with `sk-ant-`, check it's exported correctly |
| `401 Unauthorized` | Make sure JWT token is included in Authorization header |
| `Rate limit exceeded` | This is correct behavior. Wait 1 hour or change config |
| `Response validation failed` | LLM response format changed. Check mock response in LLMServiceImp |
| `Database connection refused` | Ensure PostgreSQL is running and credentials are correct |
| `Dependencies not found` | Run `./gradlew dependencies` to download all deps |

---

## 📊 Monitoring

### API Logs
```sql
-- Get all API calls in last 24h
SELECT * FROM ai_api_logs 
WHERE timestamp >= NOW() - INTERVAL '1 day'
ORDER BY timestamp DESC;

-- Get error logs
SELECT * FROM ai_api_logs 
WHERE response_status = 'ERROR'
ORDER BY timestamp DESC;

-- Get cost analysis
SELECT 
  DATE(timestamp) as date,
  COUNT(*) as calls,
  SUM(estimated_cost) as total_cost,
  AVG(response_time_ms) as avg_response_ms
FROM ai_api_logs
GROUP BY DATE(timestamp)
ORDER BY DATE(timestamp) DESC;
```

### Recommendations
```sql
-- Get recommendations by user
SELECT * FROM ai_project_recommendations 
WHERE client_id = 1
ORDER BY created_at DESC;

-- Get acceptance rate
SELECT 
  user_feedback,
  COUNT(*) as count
FROM ai_project_recommendations
GROUP BY user_feedback;

-- Get market insights
SELECT * FROM market_price_insights
WHERE category_name = 'Web Development';
```

---

## 🚀 Next Steps (Phase 2+)

1. **RAG Integration**
   - Add project templates to knowledge base
   - Include best practices for each category
   - Reference similar projects

2. **Fine-tuning**
   - Collect user feedback
   - Analyze acceptance rates
   - Update prompts based on insights

3. **Analytics Dashboard**
   - Admin panel to view stats
   - Cost analysis
   - Performance metrics

4. **Advanced Features**
   - Multi-language support
   - Image handling for project attachments
   - Estimated freelancer count
   - Skill demand trends

5. **Optimization**
   - Cache improvements
   - Async processing
   - Background jobs for market recalculation

---

## 📞 Support Resources

1. **Documentation**
   - `AI_PROJECT_ASSISTANT_REVIEW.md` - Design decisions
   - `AI_PROJECT_ASSISTANT_IMPLEMENTATION.md` - API docs
   - `AI_PROJECT_ASSISTANT_FRONTEND.md` - UI/UX guide
   - `AI_PROJECT_ASSISTANT_SUMMARY.md` - Complete overview

2. **Code Files**
   - Check package: `com.freelancemarketplace.backend.service.imp`
   - Check package: `com.freelancemarketplace.backend.dto`
   - Check package: `com.freelancemarketplace.backend.model`

3. **Database**
   - Schema file: `database_migration_ai_project_assistant.sql`
   - Views for analytics already created

---

## 🎉 You're Ready!

You now have a complete, production-ready AI Project Assistant implementation:

✅ Smart pricing engine
✅ Claude AI integration  
✅ Rate limiting & cost tracking
✅ Complete REST API (6 endpoints)
✅ Database with analytics views
✅ React frontend component
✅ Comprehensive documentation

**Estimated time to full integration: 2-3 hours**

Start with Phase 0 (get API key) and work through each phase sequentially.

Good luck! 🚀


