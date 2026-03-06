-- AI Project Assistant Database Migration
-- Run this SQL to set up the necessary tables for AI functionality

-- Table: ai_project_recommendations
-- Stores all AI-generated project recommendations for audit and learning
CREATE TABLE IF NOT EXISTS ai_project_recommendations (
    recommendation_id SERIAL PRIMARY KEY,
    client_id BIGINT NOT NULL REFERENCES clients(client_id) ON DELETE CASCADE,
    user_brief TEXT NOT NULL,
    suggested_title VARCHAR(255),
    suggested_description TEXT,
    suggested_budget_min DECIMAL(10, 2),
    suggested_budget_recommended DECIMAL(10, 2),
    suggested_budget_max DECIMAL(10, 2),
    budget_confidence DECIMAL(3, 2),
    complexity_factor DECIMAL(3, 2),
    urgency_factor DECIMAL(3, 2),
    category_id BIGINT REFERENCES categories(category_id),
    experience_level VARCHAR(50),
    region VARCHAR(100),
    market_sample_count INT,
    market_context TEXT,
    user_feedback VARCHAR(50), -- ACCEPTED, REJECTED, PARTIAL, NOT_PROVIDED
    feedback_notes TEXT,
    token_count INT,
    estimated_cost DECIMAL(6, 4),
    raw_json_response TEXT,
    llm_model VARCHAR(100),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_ai_recommendations_client_id ON ai_project_recommendations(client_id);
CREATE INDEX idx_ai_recommendations_category_id ON ai_project_recommendations(category_id);
CREATE INDEX idx_ai_recommendations_created_at ON ai_project_recommendations(created_at);

-- Table: market_price_stats
-- Caches calculated market price statistics (updated periodically)
CREATE TABLE IF NOT EXISTS market_price_stats (
    stats_id SERIAL PRIMARY KEY,
    category_id BIGINT NOT NULL REFERENCES categories(category_id) ON DELETE CASCADE,
    skill_id BIGINT REFERENCES skills(skill_id),
    scope VARCHAR(50) NOT NULL, -- SMALL, MEDIUM, LARGE, ENTERPRISE
    experience_level VARCHAR(50) NOT NULL, -- BEGINNER, INTERMEDIATE, EXPERT
    region VARCHAR(100) DEFAULT 'Global',
    p25_budget DECIMAL(10, 2), -- 25th percentile
    p50_budget DECIMAL(10, 2), -- 50th percentile (median)
    p75_budget DECIMAL(10, 2), -- 75th percentile
    sample_count INT,
    currency VARCHAR(3) DEFAULT 'USD',
    confidence DECIMAL(3, 2), -- 0.0 to 1.0
    calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT true,
    UNIQUE(category_id, skill_id, scope, experience_level, region)
);

CREATE INDEX idx_market_stats_category ON market_price_stats(category_id);
CREATE INDEX idx_market_stats_scope ON market_price_stats(scope);
CREATE INDEX idx_market_stats_experience ON market_price_stats(experience_level);
CREATE INDEX idx_market_stats_calculated_at ON market_price_stats(calculated_at);

-- Table: ai_api_logs
-- Logs all AI API calls for cost analysis and monitoring
CREATE TABLE IF NOT EXISTS ai_api_logs (
    log_id SERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(user_id) ON DELETE SET NULL,
    provider VARCHAR(50) NOT NULL, -- anthropic, openai, google
    model VARCHAR(100),
    prompt_tokens INT,
    completion_tokens INT,
    total_tokens INT,
    estimated_cost DECIMAL(8, 6),
    response_status VARCHAR(50), -- SUCCESS, ERROR, TIMEOUT
    error_message TEXT,
    response_time_ms BIGINT,
    feature VARCHAR(100), -- project_assistant, etc
    cached BOOLEAN DEFAULT false,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_ai_logs_user_id ON ai_api_logs(user_id);
CREATE INDEX idx_ai_logs_timestamp ON ai_api_logs(timestamp);
CREATE INDEX idx_ai_logs_provider ON ai_api_logs(provider);
CREATE INDEX idx_ai_logs_feature ON ai_api_logs(feature);

-- Add column to projects table (optional, for tracking AI-assisted projects)
ALTER TABLE projects ADD COLUMN IF NOT EXISTS ai_assisted BOOLEAN DEFAULT false;
ALTER TABLE projects ADD COLUMN IF NOT EXISTS ai_recommendation_id BIGINT REFERENCES ai_project_recommendations(recommendation_id);

-- View: AI Usage Statistics
CREATE OR REPLACE VIEW ai_usage_stats AS
SELECT
    u.user_id,
    u.username,
    COUNT(DISTINCT aal.log_id) as total_api_calls,
    SUM(aal.total_tokens) as total_tokens_used,
    SUM(aal.estimated_cost) as total_cost,
    AVG(aal.response_time_ms) as avg_response_time,
    COUNT(CASE WHEN aal.response_status = 'ERROR' THEN 1 END) as error_count,
    MAX(aal.timestamp) as last_usage
FROM users u
LEFT JOIN ai_api_logs aal ON u.user_id = aal.user_id
GROUP BY u.user_id, u.username;

-- View: Market Price Insights
CREATE OR REPLACE VIEW market_price_insights AS
SELECT
    c.name as category_name,
    mps.scope,
    mps.experience_level,
    mps.region,
    mps.p25_budget,
    mps.p50_budget,
    mps.p75_budget,
    mps.sample_count,
    mps.confidence,
    (mps.p50_budget * 1.3) as high_budget_estimate,
    (mps.p50_budget * 0.8) as low_budget_estimate
FROM market_price_stats mps
JOIN categories c ON mps.category_id = c.category_id
WHERE mps.is_active = true
ORDER BY c.name, mps.scope, mps.experience_level;

-- View: AI Recommendation Analytics
CREATE OR REPLACE VIEW ai_recommendation_analytics AS
SELECT
    DATE(apr.created_at) as date,
    c.name as category,
    COUNT(*) as recommendations_created,
    SUM(CASE WHEN apr.user_feedback = 'ACCEPTED' THEN 1 ELSE 0 END) as accepted_count,
    SUM(CASE WHEN apr.user_feedback = 'REJECTED' THEN 1 ELSE 0 END) as rejected_count,
    SUM(CASE WHEN apr.user_feedback = 'PARTIAL' THEN 1 ELSE 0 END) as partial_count,
    AVG(apr.budget_confidence) as avg_budget_confidence,
    SUM(apr.token_count) as total_tokens_used,
    SUM(apr.estimated_cost) as total_cost
FROM ai_project_recommendations apr
LEFT JOIN categories c ON apr.category_id = c.category_id
GROUP BY DATE(apr.created_at), c.name
ORDER BY DATE(apr.created_at) DESC;

