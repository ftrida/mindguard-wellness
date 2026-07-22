from datetime import date, datetime, time, timedelta
from typing import Optional
from app.repositories.lifestyle_repo import LifestyleRepository
from app.repositories.mood_repo import MoodRepository
from app.repositories.journal_repo import JournalRepository
from app.repositories.meditation_repo import MeditationRepository
from app.repositories.focus_repo import FocusRepository
from app.schemas.dashboard import TodaySummaryResponse, HistoricalSummaryResponse

class DashboardService:
    def __init__(
        self,
        lifestyle_repo: LifestyleRepository,
        mood_repo: MoodRepository,
        journal_repo: JournalRepository,
        meditation_repo: MeditationRepository,
        focus_repo: FocusRepository
    ):
        self.lifestyle_repo = lifestyle_repo
        self.mood_repo = mood_repo
        self.journal_repo = journal_repo
        self.meditation_repo = meditation_repo
        self.focus_repo = focus_repo

    async def get_today_summary(self, user_id: int) -> TodaySummaryResponse:
        today = date.today()
        start_of_today = datetime.combine(today, time.min)
        end_of_today = datetime.combine(today, time.max)

        # 1. Fetch lifestyle logs
        log = await self.lifestyle_repo.get_by_date(user_id, today)
        sleep = log.sleep_hours if log else 0.0
        water = log.water_intake if log else 0.0
        exercise = log.exercise_minutes if log else 0
        steps = log.walking_steps if log else 0
        screen = log.screen_time if log else 0.0

        # 2. Fetch mood average
        mood_entries = await self.mood_repo.get_range(user_id, start_of_today, end_of_today)
        mood_score = sum(m.mood_score for m in mood_entries) / len(mood_entries) if mood_entries else None

        # 3. Fetch journal entry count
        # For simplicity, we can fetch all and count those from today
        journals = await self.journal_repo.get_all_by_user_id(user_id)
        journal_count = sum(1 for j in journals if j.created_at.date() == today)

        # 4. Fetch meditation sessions
        meditation_sessions = await self.meditation_repo.get_range(user_id, start_of_today, end_of_today)
        meditation_minutes = sum(m.duration_seconds for m in meditation_sessions) / 60.0

        # 5. Fetch focus sessions
        focus_sessions = await self.focus_repo.get_range(user_id, start_of_today, end_of_today)
        focus_count = sum(f.completed_sessions_count for f in focus_sessions)

        return TodaySummaryResponse(
            sleep_hours=sleep,
            water_intake=water,
            exercise_minutes=exercise,
            walking_steps=steps,
            screen_time=screen,
            meditation_minutes=round(meditation_minutes, 2),
            focus_sessions_count=focus_count,
            mood_score_avg=round(mood_score, 2) if mood_score is not None else None,
            journal_entries_count=journal_count
        )

    async def get_historical_summary(self, user_id: int, start_date: date, end_date: date) -> HistoricalSummaryResponse:
        start_dt = datetime.combine(start_date, time.min)
        end_dt = datetime.combine(end_date, time.max)

        # 1. Fetch lifestyle logs
        logs = await self.lifestyle_repo.get_range(user_id, start_date, end_date)
        total_days = len(logs)
        avg_sleep = sum(l.sleep_hours for l in logs) / total_days if total_days else 0.0
        avg_water = sum(l.water_intake for l in logs) / total_days if total_days else 0.0
        total_exercise = sum(l.exercise_minutes for l in logs)
        total_steps = sum(l.walking_steps for l in logs)
        avg_screen = sum(l.screen_time for l in logs) / total_days if total_days else 0.0

        # 2. Fetch mood entries
        mood_entries = await self.mood_repo.get_range(user_id, start_dt, end_dt)
        avg_mood = sum(m.mood_score for m in mood_entries) / len(mood_entries) if mood_entries else None

        # 3. Fetch journal entries
        journals = await self.journal_repo.get_all_by_user_id(user_id)
        total_journals = sum(1 for j in journals if start_date <= j.created_at.date() <= end_date)

        # 4. Fetch meditation sessions
        meditations = await self.meditation_repo.get_range(user_id, start_dt, end_dt)
        total_meditation_min = sum(m.duration_seconds for m in meditations) / 60.0

        # 5. Fetch focus sessions
        focus_sessions = await self.focus_repo.get_range(user_id, start_dt, end_dt)
        total_focus = sum(f.completed_sessions_count for f in focus_sessions)

        return HistoricalSummaryResponse(
            start_date=start_date,
            end_date=end_date,
            avg_sleep_hours=round(avg_sleep, 2),
            avg_water_intake=round(avg_water, 2),
            total_exercise_minutes=total_exercise,
            total_walking_steps=total_steps,
            avg_screen_time=round(avg_screen, 2),
            total_meditation_minutes=round(total_meditation_min, 2),
            total_focus_sessions=total_focus,
            avg_mood_score=round(avg_mood, 2) if avg_mood is not None else None,
            total_journal_entries=total_journals
        )
