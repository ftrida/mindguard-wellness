import { api } from '@/lib/api';

export const apiServices = {
  // Wellness
  logLifestyle: (data: { sleep_hours: number; screen_time_hours: number; active_minutes: number }) =>
    api.post('/api/v1/lifestyle', data),
  logMood: (data: { mood_score: number; notes?: string }) =>
    api.post('/api/v1/mood', data),
  createJournal: (data: { title: string; content: string }) =>
    api.post('/api/v1/journal', data),

  // AI Intelligence
  getTwin: () => api.get('/api/v1/twin'),
  getBehaviorDrift: () => api.get('/api/v1/behavior/drift'),
  getStressAssessment: () => api.get('/api/v1/stress/assessment'),
  sendCoachMessage: (content: string) => api.post('/api/v1/coach/chat', { content }),
  getCoachAdvice: () => api.get('/api/v1/coach/advice'),
  getMemory: () => api.get('/api/v1/coach/conversation'),

  // Gamification & Reports
  getGoals: () => api.get('/api/v1/goals'),
  getAchievements: () => api.get('/api/v1/achievements'),
  getStreak: () => api.get('/api/v1/achievements/streak'),
  getDailyReport: () => api.get('/api/v1/reports/daily'),

  // Admin & Monitoring
  getHealth: () => api.get('/health'),
  getSystemHealth: () => api.get('/health/system'),
  getSchedulerHealth: () => api.get('/health/scheduler'),
};
