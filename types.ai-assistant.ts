// API Response Types & Interfaces for AI Project Assistant

// ==================== REQUEST TYPES ====================

export interface ProjectAssistantRequestPayload {
  brief: string; // 10-2000 characters
  categoryId: number;
  scope: 'SMALL' | 'MEDIUM' | 'LARGE' | 'ENTERPRISE';
  timeline: 'Less than 1 month' | '1 to 3 months' | '3 to 6 months' | 'More than 6 months';
  experienceLevel?: 'BEGINNER' | 'INTERMEDIATE' | 'EXPERT';
  preferredSkills?: string[];
  region?: string;
  complexityHint?: 'LOW' | 'MEDIUM' | 'HIGH';
}

export interface ImproveProjectFeedbackPayload {
  feedback: string; // User's feedback for improvement
}

export interface RecordFeedbackPayload {
  feedback: 'ACCEPTED' | 'REJECTED' | 'PARTIAL';
  notes?: string;
}

// ==================== RESPONSE TYPES ====================

export interface AIProjectAssistantResponse {
  success: boolean;
  message: string;
  timestamp: string; // ISO-8601
  body: AIProjectAssistantBody | null;
}

export interface AIProjectAssistantBody {
  requestId: string;
  projectDraft: ProjectDraft;
  budgetSuggestion: BudgetSuggestion;
  advancedPreferencesSuggestion: AdvancedPreferencesSuggestion;
  clarifyingQuestions: string[];
  warnings: string[];
}

export interface ProjectDraft {
  title: string;
  description: string;
  categoryId: number;
  scope: 'SMALL' | 'MEDIUM' | 'LARGE' | 'ENTERPRISE';
  skills: string[];
  timelineDays: number;
}

export interface BudgetSuggestion {
  currency: string; // e.g., "USD"
  min: number;
  recommended: number;
  max: number;
  confidence: number; // 0.0-1.0
  pricingSource: string; // e.g., "market_stats_v1"
  marketSummary: string;
  factors: BudgetFactors;
}

export interface BudgetFactors {
  scopeWeight: number;
  complexityWeight: number;
  experienceWeight: number;
  urgencyWeight: number;
  locationWeight: number;
}

export interface AdvancedPreferencesSuggestion {
  experienceLevel: 'BEGINNER' | 'INTERMEDIATE' | 'EXPERT';
  projectDuration: number; // Days
  hoursPerWeek: number;
  freelancerType: 'INDIVIDUAL' | 'TEAM';
  locationPreference: string;
  timezoneOverlap: string;
  englishLevel: string;
  projectType: 'ONE_TIME' | 'ONGOING' | 'RETAINER';
  complexity: 'LOW' | 'MEDIUM' | 'HIGH';
  urgent: boolean;
}

// ==================== HISTORY RESPONSE ====================

export interface HistoryResponseDTO {
  statusCode: string;
  statusMessage: string;
  body: HistoryItem[];
}

export interface HistoryItem {
  recommendationId: number;
  userBrief: string;
  suggestedTitle: string;
  suggestedDescription: string;
  suggestedBudgetMin: number;
  suggestedBudgetRecommended: number;
  suggestedBudgetMax: number;
  userFeedback: 'ACCEPTED' | 'REJECTED' | 'PARTIAL' | 'NOT_PROVIDED' | null;
  createdAt: string; // ISO-8601
}

// ==================== STATS RESPONSE ====================

export interface StatsResponseDTO {
  statusCode: string;
  statusMessage: string;
  body: UserStats;
}

export interface UserStats {
  api_calls_24h: number;
  estimated_cost_24h: number;
  recent_recommendations: number;
  accepted_recommendations: number;
}

// ==================== GENERIC FEEDBACK RESPONSE ====================

export interface GenericResponseDTO {
  statusCode: string;
  statusMessage: string;
}

// ==================== HEALTH CHECK ====================

export interface HealthCheckResponse {
  status: string; // "UP" or "DOWN"
  service: string; // "AI Project Assistant"
}

// ==================== ERROR RESPONSE ====================

export interface ErrorResponse {
  success: false;
  message: string;
  timestamp: string;
  body: null;
}

// ==================== API SERVICE CLASS ====================

export class AIProjectAssistantAPI {
  private baseURL: string;
  private token: string;

  constructor(baseURL: string, token: string) {
    this.baseURL = baseURL;
    this.token = token;
  }

  private getHeaders() {
    return {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${this.token}`
    };
  }

  /**
   * Generate project suggestions
   */
  async suggestProject(
    request: ProjectAssistantRequestPayload
  ): Promise<AIProjectAssistantBody> {
    const response = await fetch(`${this.baseURL}/suggest`, {
      method: 'POST',
      headers: this.getHeaders(),
      body: JSON.stringify(request)
    });

    const data: AIProjectAssistantResponse = await response.json();

    if (!response.ok || !data.success) {
      throw new Error(data.message || 'Failed to generate suggestions');
    }

    return data.body!;
  }

  /**
   * Improve existing suggestions
   */
  async improveProject(
    recommendationId: number,
    feedback: string
  ): Promise<AIProjectAssistantBody> {
    const response = await fetch(
      `${this.baseURL}/improve/${recommendationId}`,
      {
        method: 'POST',
        headers: this.getHeaders(),
        body: JSON.stringify({ feedback })
      }
    );

    const data: AIProjectAssistantResponse = await response.json();

    if (!response.ok || !data.success) {
      throw new Error(data.message || 'Failed to improve suggestions');
    }

    return data.body!;
  }

  /**
   * Record user feedback
   */
  async recordFeedback(
    recommendationId: number,
    payload: RecordFeedbackPayload
  ): Promise<void> {
    const response = await fetch(
      `${this.baseURL}/feedback/${recommendationId}`,
      {
        method: 'POST',
        headers: this.getHeaders(),
        body: JSON.stringify(payload)
      }
    );

    const data: GenericResponseDTO = await response.json();

    if (!response.ok) {
      throw new Error(data.statusMessage || 'Failed to record feedback');
    }
  }

  /**
   * Get recommendation history
   */
  async getHistory(page: number = 0, size: number = 10): Promise<HistoryItem[]> {
    const response = await fetch(
      `${this.baseURL}/history?page=${page}&size=${size}`,
      {
        headers: this.getHeaders()
      }
    );

    const data: HistoryResponseDTO = await response.json();

    if (!response.ok) {
      throw new Error(data.statusMessage || 'Failed to get history');
    }

    return data.body;
  }

  /**
   * Get usage statistics
   */
  async getStats(): Promise<UserStats> {
    const response = await fetch(`${this.baseURL}/stats`, {
      headers: this.getHeaders()
    });

    const data: StatsResponseDTO = await response.json();

    if (!response.ok) {
      throw new Error(data.statusMessage || 'Failed to get stats');
    }

    return data.body;
  }

  /**
   * Health check (no auth needed)
   */
  static async healthCheck(baseURL: string): Promise<HealthCheckResponse> {
    const response = await fetch(`${baseURL}/health`);

    if (!response.ok) {
      throw new Error('Health check failed');
    }

    return await response.json();
  }
}

// ==================== USAGE EXAMPLE ====================

/*
// In your React component or service
import { AIProjectAssistantAPI, ProjectAssistantRequestPayload } from './types/ai';

// Initialize API client
const api = new AIProjectAssistantAPI(
  'http://localhost:8080/api/ai/project-assistant',
  localStorage.getItem('token') || ''
);

// Use in component
async function handleSuggest() {
  try {
    const request: ProjectAssistantRequestPayload = {
      brief: "Build an e-commerce website",
      categoryId: 1,
      scope: 'MEDIUM',
      timeline: '1 to 3 months',
      experienceLevel: 'INTERMEDIATE'
    };

    const suggestions = await api.suggestProject(request);

    console.log('Title:', suggestions.projectDraft.title);
    console.log('Budget:', suggestions.budgetSuggestion.recommended);
    console.log('Skills:', suggestions.projectDraft.skills);

  } catch (error) {
    console.error('Error:', error.message);
  }
}

// Health check before app starts
async function checkAPI() {
  try {
    const health = await AIProjectAssistantAPI.healthCheck(
      'http://localhost:8080/api/ai/project-assistant'
    );
    console.log('API Status:', health.status);
  } catch (error) {
    console.error('API is down:', error.message);
  }
}
*/

