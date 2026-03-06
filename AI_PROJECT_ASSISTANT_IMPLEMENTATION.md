# AI Project Assistant - Implementation Guide

## 📋 Overview

The AI Project Assistant is a comprehensive feature that helps clients (buyers) on your freelancer marketplace write better project briefs by leveraging AI and market data. It combines:

1. **Pricing Engine** - Calculates market-based budget recommendations
2. **LLM Integration** - Uses Claude AI to generate content suggestions
3. **Rate Limiting** - Prevents abuse
4. **Cost Tracking** - Monitors API usage and costs
5. **Audit Trail** - Stores all recommendations for learning

---

## 🚀 Quick Start

### 1. Setup Environment Variables

Add these to your `.env` file:

```bash
# AI LLM Configuration
AI_LLM_API_KEY=sk-ant-your-api-key-here

# Get your API key from: https://console.anthropic.com
```

### 2. Install Dependencies

The dependencies have been added to `build.gradle.kts`. Run:

```bash
./gradlew clean build
```

### 3. Run Database Migration

Execute the SQL migration file:

```sql
psql -U your_user -d your_database -f database_migration_ai_project_assistant.sql
```

Or if using Spring Boot's `ddl-auto: update`, the tables will be created automatically.

### 4. Start the Application

```bash
./gradlew bootRun
```

---

## 📊 API Endpoints

### 1. Generate Project Suggestions (Main Endpoint)

**POST** `/api/ai/project-assistant/suggest`

**Request:**
```json
{
  "brief": "I need to build an e-commerce website with payment integration and user authentication",
  "categoryId": 5,
  "scope": "MEDIUM",
  "timeline": "1 to 3 months",
  "preferredSkills": ["React", "Node.js"],
  "experienceLevel": "INTERMEDIATE",
  "region": null,
  "complexityHint": "MEDIUM"
}
```

**Response:**
```json
{
  "statusCode": "200",
  "statusMessage": "Project suggestions generated successfully",
  "body": {
    "title": "Professional E-Commerce Platform Development",
    "description": "Build a scalable e-commerce platform with modern tech stack...",
    "skills": [
      {
        "name": "React",
        "priority": "HIGH",
        "reason": "Modern frontend framework for responsive UI"
      },
      {
        "name": "Node.js",
        "priority": "HIGH",
        "reason": "Backend development and API creation"
      }
    ],
    "budget": {
      "min": 3000,
      "recommended": 5000,
      "max": 8000,
      "confidence": 0.85,
      "explanation": "Based on 45 similar projects in Web Development category, adjusted for medium scope and 1-3 month timeline",
      "marketContext": "Median budget for medium-scope web projects is $5000"
    },
    "milestones": [
      {
        "title": "Project Setup & Planning",
        "description": "Environment setup, architecture design, database schema planning",
        "daysFromStart": 7,
        "budgetPercentage": 10
      },
      {
        "title": "Frontend Development",
        "description": "React components, UI implementation, responsive design",
        "daysFromStart": 45,
        "budgetPercentage": 40
      }
    ],
    "clarifyingQuestions": [
      "What payment gateways do you prefer? (Stripe, PayPal, etc.)",
      "Do you need multi-vendor support?",
      "What's your expected number of daily users?"
    ],
    "confidence": 0.82,
    "reasoning": "This recommendation is based on 45 similar projects completed in the marketplace, adjusted for your specific requirements"
  }
}
```

**Status Codes:**
- `200` - Success
- `400` - Bad request (validation error, rate limit exceeded)
- `401` - Unauthorized
- `500` - Server error

---

### 2. Improve Existing Draft

**POST** `/api/ai/project-assistant/improve/{recommendationId}`

**Request:**
```json
{
  "feedback": "I think the budget might be too high. Can we reduce it? Also, I don't need multi-vendor support."
}
```

**Response:** Same format as suggestion endpoint

---

### 3. Record User Feedback

**POST** `/api/ai/project-assistant/feedback/{recommendationId}`

**Request:**
```json
{
  "feedback": "ACCEPTED",
  "notes": "Great suggestions! Will use most of them."
}
```

Feedback types:
- `ACCEPTED` - User accepted all suggestions
- `REJECTED` - User rejected all suggestions
- `PARTIAL` - User accepted some suggestions

**Response:**
```json
{
  "statusCode": "200",
  "statusMessage": "Feedback recorded successfully"
}
```

---

### 4. Get Recommendation History

**GET** `/api/ai/project-assistant/history?page=0&size=10`

**Response:**
```json
{
  "statusCode": "200",
  "statusMessage": "Recommendation history retrieved",
  "body": [
    {
      "recommendationId": 1,
      "userBrief": "...",
      "suggestedTitle": "...",
      "userFeedback": "ACCEPTED",
      "createdAt": "2024-03-06T10:30:00Z"
    }
  ]
}
```

---

### 5. Get Usage Statistics

**GET** `/api/ai/project-assistant/stats`

**Response:**
```json
{
  "statusCode": "200",
  "statusMessage": "Usage statistics retrieved",
  "body": {
    "api_calls_24h": 3,
    "estimated_cost_24h": "0.0045",
    "recent_recommendations": 3,
    "accepted_recommendations": 2
  }
}
```

---

### 6. Health Check

**GET** `/api/ai/project-assistant/health`

**Response:**
```json
{
  "status": "UP",
  "service": "AI Project Assistant"
}
```

---

## ⚙️ Configuration

### application-dev.yml

```yaml
ai:
  llm:
    provider: anthropic              # LLM provider
    model: claude-3-5-haiku-20241022 # Model name
    api-key: ${AI_LLM_API_KEY}      # API key from environment
    max-tokens: 1500                # Max response tokens
    temperature: 0.7                # Creativity (0.0-1.0)
    timeout: 30                     # Seconds
  
  pricing:
    cache-ttl: 24                   # Cache duration (hours)
    min-sample-size: 5              # Min projects for stats
    confidence:
      low-threshold: 0.3
      medium-threshold: 0.7
      high-threshold: 0.9
  
  rate-limit:
    requests-per-hour: 10           # Max requests per user
    enabled: true
```

---

## 📦 Request/Response Validation

### Input Validation

```yaml
ProjectAssistantRequest:
  brief:
    - Required
    - Min: 10 characters
    - Max: 2000 characters
  categoryId:
    - Required
    - Must be valid category ID
  scope:
    - Required
    - Values: SMALL, MEDIUM, LARGE, ENTERPRISE
  timeline:
    - Required
    - Values: Less than 1 month, 1 to 3 months, 3 to 6 months, More than 6 months
  experienceLevel:
    - Optional
    - Default: INTERMEDIATE
    - Values: BEGINNER, INTERMEDIATE, EXPERT
```

### Output Validation

The LLM response is validated against:
1. Valid JSON schema
2. Required fields presence
3. Budget range (±30% of market data)
4. No suspicious claims (guaranteed wins, 100% success, etc.)
5. Skills exist in marketplace
6. No harmful content

---

## 💰 Cost Analysis

### Pricing Breakdown (Claude 3.5 Haiku)

- Input: $0.80 per 1M tokens
- Output: $4.00 per 1M tokens

### Typical Request Cost

- Average input: 500 tokens = $0.0004
- Average output: 200 tokens = $0.0008
- **Per request: ~$0.0012**

### Monthly Cost Estimate

- 10,000 requests/month × $0.0012 = **$12/month**
- Database: $10/month
- Cache: $5/month
- **Total: ~$27/month** (very affordable!)

### Cost Optimization

1. **Cache market stats** (24h) - Avoids database queries
2. **Token counting** - Pre-calculate costs
3. **Fallback models** - Use cheaper models for simple cases
4. **Rate limiting** - Prevents abuse

---

## 🔒 Security & Guardrails

### Input Guardrails

✅ SQL injection prevention - Using parameterized queries
✅ XSS prevention - JSON serialization
✅ Rate limiting - 10 requests/hour per user
✅ Budget validation - Within market range ±30%
✅ Content filtering - No malicious content

### Output Guardrails

✅ JSON schema validation
✅ No guaranteed claims (no "100% success" promises)
✅ Prices from market data only (no LLM-invented prices)
✅ Skills validation against existing skills
✅ Confidence score transparency

### API Key Security

```bash
# Store in environment variables (NOT in code)
AI_LLM_API_KEY=sk-ant-...

# .env file
export AI_LLM_API_KEY="your-key-here"

# .gitignore
.env
*.env
```

---

## 🧪 Testing

### Manual Testing

1. **Generate Suggestions**
```bash
curl -X POST http://localhost:8080/api/ai/project-assistant/suggest \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "brief": "Build a mobile app",
    "categoryId": 1,
    "scope": "MEDIUM",
    "timeline": "1 to 3 months"
  }'
```

2. **Record Feedback**
```bash
curl -X POST http://localhost:8080/api/ai/project-assistant/feedback/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"feedback": "ACCEPTED", "notes": "Great!"}'
```

3. **Get Stats**
```bash
curl http://localhost:8080/api/ai/project-assistant/stats \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Unit Testing (TODO)

```java
@Test
void testSuggestProjectContent() {
    ProjectAssistantRequest request = new ProjectAssistantRequest();
    request.setBrief("Build a website");
    request.setCategoryId(1L);
    request.setScope("MEDIUM");
    request.setTimeline("1 to 3 months");
    
    ProjectAssistantResponse response = 
        aiProjectAssistantService.suggestProjectContent(userId, request);
    
    assertNotNull(response.getSuggestedTitle());
    assertNotNull(response.getBudgetSuggestion());
    assertTrue(response.getOverallConfidence() > 0);
}
```

---

## 📈 Monitoring & Analytics

### View Usage Statistics

```sql
SELECT * FROM ai_usage_stats;
```

### View Market Insights

```sql
SELECT * FROM market_price_insights;
```

### View Recommendation Analytics

```sql
SELECT * FROM ai_recommendation_analytics;
```

### Monitor API Logs

```sql
SELECT * FROM ai_api_logs 
WHERE timestamp >= NOW() - INTERVAL '24 hours'
ORDER BY timestamp DESC;
```

---

## 🔧 Troubleshooting

### Issue: Rate Limit Exceeded

**Error:** "Rate limit exceeded. Maximum 10 requests per hour."

**Solution:**
- Wait 1 hour for the limit to reset
- Or admin can increase `rate-limit.requests-per-hour` in config

### Issue: API Key Invalid

**Error:** "Failed to generate AI suggestions: API key invalid"

**Solution:**
1. Check `AI_LLM_API_KEY` in environment
2. Verify it starts with `sk-ant-`
3. Get new key from https://console.anthropic.com

### Issue: LLM Response Validation Failed

**Error:** "AI response validation failed. Please try again."

**Solution:**
- This happens if LLM returns malformed JSON or violates guardrails
- Try rewording the brief or changing parameters
- Check logs for details

### Issue: Budget is NULL

**Error:** Budget recommendations coming back as null

**Solution:**
- Market stats might not be available for this category/scope
- Admin needs to run: `pricingEngineService.recalculateMarketStats(categoryId)`
- Or ensure there are completed projects in this category

---

## 📚 Data Models

### AIProjectRecommendationModel
Stores all AI recommendations with:
- User brief and AI suggestions
- Budget recommendations and confidence
- Market context and sample count
- User feedback for learning
- Raw LLM response for debugging

### MarketPriceStatsModel
Caches market statistics:
- Percentiles (p25, p50, p75)
- Sample count and confidence
- Category, scope, experience level, region
- Updated periodically

### AIAPILogModel
Tracks API usage:
- Provider and model used
- Token counts and estimated cost
- Response status and timing
- Feature and caching status

---

## 🚀 Next Steps (Phase 2+)

1. **RAG/Knowledge Base** - Add project templates and examples
2. **Fine-tuning** - Collect feedback to improve prompts
3. **Multi-language** - Support non-English briefs
4. **Advanced Analytics** - Dashboard for admin
5. **A/B Testing** - Test different prompts
6. **Webhooks** - Real-time notifications
7. **Admin Panel** - Manage prompts and pricing

---

## 📞 Support

For issues or questions:
1. Check the troubleshooting section
2. Review logs: `docker logs freelancer-backend`
3. Check database: `psql -d freelancer_db -c "SELECT * FROM ai_api_logs LIMIT 10;"`
4. Contact development team

---

## ✅ Deployment Checklist

- [ ] Set `AI_LLM_API_KEY` environment variable
- [ ] Run database migration
- [ ] Update `application.yml` with correct settings
- [ ] Test endpoints with valid JWT token
- [ ] Monitor first 100 requests
- [ ] Alert on errors > 5%
- [ ] Monitor costs daily


