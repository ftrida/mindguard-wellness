from datetime import date
from typing import Optional
from pydantic import BaseModel

class TodaySummaryResponse(BaseModel):
    sleep_hours: float
    water_intake: float
    exercise_minutes: int
    walking_steps: int
    screen_time: float
    meditation_minutes: float
    focus_sessions_count: int
    mood_score_avg: Optional[float]
    journal_entries_count: int

class HistoricalSummaryResponse(BaseModel):
    start_date: date
    end_date: date
    avg_sleep_hours: float
    avg_water_intake: float
    total_exercise_minutes: int
    total_walking_steps: int
    avg_screen_time: float
    total_meditation_minutes: float
    total_focus_sessions: int
    avg_mood_score: Optional[float]
    total_journal_entries: int
