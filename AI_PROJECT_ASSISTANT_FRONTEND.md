# AI Project Assistant - Frontend Integration Guide

## 📱 UI/UX Components

### 1. Project Creation Flow

```
┌─────────────────────────────────────────┐
│  Step 1: Write Project Brief            │
│  ┌──────────────────────────────────┐   │
│  │ Describe your project briefly:   │   │
│  │ [Text area - min 10 chars]       │   │
│  │                                  │   │
│  │ Category: [Dropdown ▼]           │   │
│  │ Scope: [Radio SMALL/MEDIUM/...]  │   │
│  │ Timeline: [Dropdown ▼]           │   │
│  │ Experience Level: [Dropdown ▼]   │   │
│  │                                  │   │
│  │ [ 🤖 Generate with AI ]          │   │ ← Main button
│  │ [ Skip ]                         │   │
│  └──────────────────────────────────┘   │
└─────────────────────────────────────────┘
        ↓ (on click "Generate with AI")
┌─────────────────────────────────────────┐
│  Step 2: Review AI Suggestions           │
│  ┌──────────────────────────────────┐   │
│  │ Suggested Title:                 │   │
│  │ "Build E-Commerce Platform"      │   │
│  │ [Edit] [✓ Accept]                │   │
│  │                                  │   │
│  │ Suggested Description:           │   │
│  │ [Preview] [Edit] [✓ Accept]      │   │
│  │                                  │   │
│  │ Skills: React, Node.js, ...      │   │
│  │ [Edit] [✓ Accept]                │   │
│  │                                  │   │
│  │ Budget Suggestion:               │   │
│  │ Min: $3000                       │   │
│  │ Recommended: $5000 ⭐ (85% conf) │   │
│  │ Max: $8000                       │   │
│  │ 📊 Why this budget?              │   │ ← Transparency
│  │ [Edit] [✓ Accept]                │   │
│  │                                  │   │
│  │ Milestones:                      │   │
│  │ □ Project Setup (7 days, 10%)    │   │
│  │ □ Frontend Dev (45 days, 40%)    │   │
│  │ □ Backend Dev (30 days, 35%)     │   │
│  │ [Edit] [✓ Accept All]            │   │
│  │                                  │   │
│  │ Clarifying Questions:            │   │
│  │ ❓ What payment gateways?        │   │
│  │ ❓ Multi-vendor support?         │   │
│  │ ❓ Expected daily users?         │   │
│  │                                  │   │
│  │ [ 🔄 Improve My Draft ][ Continue ]│  │
│  └──────────────────────────────────┘   │
└─────────────────────────────────────────┘
        ↓
└─────────────────────────────────────────┐
│  Step 3: Finalize & Post                 │
│  [Final Review] [Post Project]           │
└─────────────────────────────────────────┘
```

---

## 🔧 Frontend Implementation

### React Component Example

```jsx
// ProjectAssistantModal.jsx
import React, { useState } from 'react';
import axios from 'axios';

export default function ProjectAssistantModal({ onProjectGenerated }) {
  const [step, setStep] = useState(1); // 1: Input, 2: Review, 3: Done
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  
  const [formData, setFormData] = useState({
    brief: '',
    categoryId: '',
    scope: 'MEDIUM',
    timeline: '1 to 3 months',
    experienceLevel: 'INTERMEDIATE',
    preferredSkills: []
  });
  
  const [suggestions, setSuggestions] = useState(null);
  const [accepted, setAccepted] = useState({
    title: false,
    description: false,
    skills: false,
    budget: false,
    milestones: false
  });

  // Step 1: Generate suggestions
  const handleGenerateSuggestions = async () => {
    if (!formData.brief || formData.brief.length < 10) {
      setError('Brief must be at least 10 characters');
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await axios.post(
        '/api/ai/project-assistant/suggest',
        formData,
        {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        }
      );

      if (response.data.statusCode === '200') {
        setSuggestions(response.data.body);
        setStep(2);
      } else {
        setError(response.data.statusMessage);
      }
    } catch (err) {
      setError(err.response?.data?.statusMessage || 'Failed to generate suggestions');
    } finally {
      setLoading(false);
    }
  };

  // Step 2: Improve suggestions
  const handleImproveSuggestions = async (feedback) => {
    setLoading(true);
    try {
      const response = await axios.post(
        `/api/ai/project-assistant/improve/${suggestions.recommendationId}`,
        { feedback },
        {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        }
      );

      if (response.data.statusCode === '200') {
        setSuggestions(response.data.body);
        setAccepted({
          title: false,
          description: false,
          skills: false,
          budget: false,
          milestones: false
        });
      }
    } catch (err) {
      setError('Failed to improve suggestions');
    } finally {
      setLoading(false);
    }
  };

  // Record feedback
  const handleRecordFeedback = async (feedbackType) => {
    try {
      await axios.post(
        `/api/ai/project-assistant/feedback/${suggestions.recommendationId}`,
        { 
          feedback: feedbackType,
          notes: 'Accepted and using suggestions'
        },
        {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        }
      );
    } catch (err) {
      console.error('Failed to record feedback', err);
    }
  };

  // Step 3: Use suggestions to create project
  const handleCreateProject = async () => {
    const projectData = {
      title: suggestions.title,
      description: suggestions.description,
      categoryId: formData.categoryId,
      scope: formData.scope,
      timeline: formData.timeline,
      skills: suggestions.skills.map(s => s.name),
      budget: {
        type: 'RANGE',
        minValue: suggestions.budget.min,
        maxValue: suggestions.budget.max
      },
      milestones: suggestions.milestones,
      aiAssisted: true,
      aiRecommendationId: suggestions.recommendationId
    };

    try {
      const response = await axios.post(
        '/api/projects',
        projectData,
        {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        }
      );

      // Record that suggestions were accepted
      await handleRecordFeedback('ACCEPTED');

      onProjectGenerated(response.data.body);
      setStep(3);
    } catch (err) {
      setError('Failed to create project');
    }
  };

  return (
    <div className="modal">
      {step === 1 && (
        <div className="step-1">
          <h2>🤖 AI Project Assistant</h2>
          <p>Describe your project and let AI help you write the perfect brief</p>

          <textarea
            value={formData.brief}
            onChange={(e) => setFormData({ ...formData, brief: e.target.value })}
            placeholder="Describe your project briefly (min 10 characters)..."
            rows={6}
            className="form-control"
          />

          <div className="form-row">
            <select
              value={formData.categoryId}
              onChange={(e) => setFormData({ ...formData, categoryId: e.target.value })}
              className="form-control"
            >
              <option value="">Select Category</option>
              <option value="1">Web Development</option>
              <option value="2">Mobile Development</option>
              <option value="3">Design</option>
            </select>

            <select
              value={formData.scope}
              onChange={(e) => setFormData({ ...formData, scope: e.target.value })}
              className="form-control"
            >
              <option>SMALL</option>
              <option>MEDIUM</option>
              <option>LARGE</option>
              <option>ENTERPRISE</option>
            </select>
          </div>

          {error && <div className="alert alert-danger">{error}</div>}

          <div className="button-group">
            <button
              onClick={handleGenerateSuggestions}
              disabled={loading}
              className="btn btn-primary btn-lg"
            >
              {loading ? 'Generating...' : '✨ Generate with AI'}
            </button>
            <button onClick={() => setStep(3)} className="btn btn-secondary">
              Skip & Post
            </button>
          </div>
        </div>
      )}

      {step === 2 && suggestions && (
        <div className="step-2">
          <h2>📋 Review AI Suggestions</h2>

          {/* Title Section */}
          <section className="suggestion-section">
            <h3>Suggested Title</h3>
            <p className="suggestion-text">{suggestions.title}</p>
            <div className="button-group">
              <button 
                onClick={() => setAccepted({ ...accepted, title: !accepted.title })}
                className={accepted.title ? 'btn-check active' : 'btn-check'}
              >
                ✓ Accept
              </button>
              <button className="btn-secondary">Edit</button>
            </div>
          </section>

          {/* Description Section */}
          <section className="suggestion-section">
            <h3>Suggested Description</h3>
            <p className="suggestion-text">{suggestions.description}</p>
            <div className="button-group">
              <button 
                onClick={() => setAccepted({ ...accepted, description: !accepted.description })}
                className={accepted.description ? 'btn-check active' : 'btn-check'}
              >
                ✓ Accept
              </button>
              <button className="btn-secondary">Edit</button>
            </div>
          </section>

          {/* Budget Section - With Transparency */}
          <section className="suggestion-section budget-section">
            <h3>💰 Budget Recommendation</h3>
            <div className="budget-display">
              <div className="budget-range">
                <span>Min: ${suggestions.budget.min}</span>
                <span>Recommended: ${suggestions.budget.recommended} ⭐</span>
                <span>Max: ${suggestions.budget.max}</span>
              </div>
              <div className="confidence-bar">
                <div 
                  className="confidence-fill"
                  style={{ width: `${suggestions.budget.confidence * 100}%` }}
                />
                <span>{(suggestions.budget.confidence * 100).toFixed(0)}% confidence</span>
              </div>
            </div>

            {/* Why This Budget - Transparency */}
            <details className="budget-reasoning">
              <summary>📊 Why this budget?</summary>
              <p>{suggestions.budget.explanation}</p>
              <p className="market-context">{suggestions.budget.marketContext}</p>
            </details>

            <div className="button-group">
              <button 
                onClick={() => setAccepted({ ...accepted, budget: !accepted.budget })}
                className={accepted.budget ? 'btn-check active' : 'btn-check'}
              >
                ✓ Accept
              </button>
              <button className="btn-secondary">Edit</button>
            </div>
          </section>

          {/* Skills Section */}
          <section className="suggestion-section">
            <h3>Skills Needed</h3>
            <div className="skills-grid">
              {suggestions.skills.map((skill, idx) => (
                <div key={idx} className={`skill-badge priority-${skill.priority}`}>
                  <span>{skill.name}</span>
                  <small>{skill.reason}</small>
                </div>
              ))}
            </div>
            <div className="button-group">
              <button 
                onClick={() => setAccepted({ ...accepted, skills: !accepted.skills })}
                className={accepted.skills ? 'btn-check active' : 'btn-check'}
              >
                ✓ Accept
              </button>
              <button className="btn-secondary">Edit</button>
            </div>
          </section>

          {/* Clarifying Questions */}
          <section className="suggestion-section">
            <h3>❓ Clarifying Questions</h3>
            <ul className="questions-list">
              {suggestions.clarifyingQuestions.map((q, idx) => (
                <li key={idx}>{q}</li>
              ))}
            </ul>
            <p className="hint">Consider answering these in your project description!</p>
          </section>

          {error && <div className="alert alert-danger">{error}</div>}

          <div className="button-group">
            <button 
              onClick={() => handleImproveSuggestions('Please adjust the budget to be lower and focus on core features first')}
              disabled={loading}
              className="btn btn-secondary"
            >
              🔄 Improve My Draft
            </button>
            <button 
              onClick={handleCreateProject}
              disabled={loading}
              className="btn btn-primary btn-lg"
            >
              {loading ? 'Creating...' : '✓ Create Project'}
            </button>
          </div>
        </div>
      )}

      {step === 3 && (
        <div className="step-3">
          <h2>✅ Success!</h2>
          <p>Your project has been created with AI-generated suggestions.</p>
          <p>Now you can:</p>
          <ul>
            <li>View proposals from freelancers</li>
            <li>Chat with interested freelancers</li>
            <li>Hire and manage your project</li>
          </ul>
          <button onClick={() => window.location.href = '/projects'} className="btn btn-primary">
            View My Projects
          </button>
        </div>
      )}
    </div>
  );
}
```

### CSS Styling

```css
.modal {
  max-width: 600px;
  margin: 0 auto;
  padding: 2rem;
  border: 1px solid #ddd;
  border-radius: 8px;
}

.step-1, .step-2, .step-3 {
  animation: fadeIn 0.3s ease-in;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

textarea, select {
  width: 100%;
  padding: 0.75rem;
  margin: 0.5rem 0;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
}

.suggestion-section {
  background: #f9f9f9;
  padding: 1.5rem;
  margin: 1rem 0;
  border-radius: 8px;
  border-left: 4px solid #0066cc;
}

.suggestion-text {
  font-size: 1.1rem;
  line-height: 1.6;
  margin: 1rem 0;
  color: #333;
}

.budget-section {
  border-left-color: #28a745;
}

.budget-display {
  background: white;
  padding: 1rem;
  border-radius: 4px;
  margin: 1rem 0;
}

.budget-range {
  display: flex;
  justify-content: space-around;
  margin-bottom: 1rem;
  font-weight: 600;
}

.confidence-bar {
  background: #e0e0e0;
  height: 20px;
  border-radius: 10px;
  overflow: hidden;
  position: relative;
}

.confidence-fill {
  background: linear-gradient(90deg, #0066cc, #00cc66);
  height: 100%;
  transition: width 0.3s ease;
}

.confidence-bar span {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 0.8rem;
  font-weight: 600;
  color: white;
}

.budget-reasoning {
  background: white;
  padding: 1rem;
  margin: 1rem 0;
  border-radius: 4px;
  cursor: pointer;
}

.budget-reasoning summary {
  font-weight: 600;
  user-select: none;
}

.budget-reasoning p {
  margin: 0.5rem 0;
  font-size: 0.95rem;
  color: #666;
}

.market-context {
  font-style: italic;
  color: #999;
}

.skills-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(120px, 1fr));
  gap: 0.5rem;
  margin: 1rem 0;
}

.skill-badge {
  padding: 0.5rem;
  background: white;
  border: 1px solid #ddd;
  border-radius: 4px;
  text-align: center;
  font-size: 0.85rem;
}

.skill-badge.priority-HIGH {
  border-color: #ff6b6b;
  background: #ffe0e0;
  font-weight: 600;
}

.skill-badge.priority-MEDIUM {
  border-color: #ffa500;
  background: #fff5e0;
}

.skill-badge.priority-LOW {
  border-color: #ddd;
  background: #f9f9f9;
}

.skill-badge small {
  display: block;
  margin-top: 0.3rem;
  color: #666;
}

.questions-list {
  list-style: none;
  padding: 0;
}

.questions-list li {
  padding: 0.75rem;
  margin: 0.5rem 0;
  background: white;
  border-left: 3px solid #ffa500;
  border-radius: 4px;
}

.button-group {
  display: flex;
  gap: 0.5rem;
  margin: 1.5rem 0;
}

.btn {
  padding: 0.75rem 1.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.btn-primary {
  background: #0066cc;
  color: white;
  flex: 1;
}

.btn-secondary {
  background: #f0f0f0;
  color: #333;
}

.btn-check {
  background: #e8f5e9;
  color: #2e7d32;
  border: 2px solid #2e7d32;
  flex: 0 1 auto;
}

.btn-check.active {
  background: #2e7d32;
  color: white;
}

.btn-lg {
  font-size: 1.1rem;
  padding: 1rem 2rem;
}

.alert {
  padding: 1rem;
  margin: 1rem 0;
  border-radius: 4px;
  border: 1px solid;
}

.alert-danger {
  background: #ffebee;
  border-color: #ff6b6b;
  color: #c62828;
}

.hint {
  font-size: 0.9rem;
  color: #999;
  font-style: italic;
}
```

---

## 🔗 Integration Points

### 1. In Project Creation Form

Add AI button alongside manual entry

### 2. In Project Dashboard

Show which projects were AI-assisted

### 3. In Analytics

Track AI usage and acceptance rates

### 4. In Notifications

Alert users about trending suggestions

---

## 💡 UX Best Practices

1. **Transparency** - Always show why budget is recommended
2. **Editing** - Allow users to tweak suggestions
3. **Iterative** - "Improve" button for refinement
4. **Feedback** - Ask users what they think
5. **Fallback** - Always allow skipping AI
6. **Mobile** - Ensure responsive design


