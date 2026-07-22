from datetime import date, datetime, timedelta
from typing import List, Tuple
from app.repositories.achievement_repo import AchievementRepository
from app.repositories.lifestyle_repo import LifestyleRepository
from app.repositories.meditation_repo import MeditationRepository
from app.models.achievement import Achievement
from app.schemas.achievement import StreakResponse

class AchievementService:
    def __init__(
        self,
        achievement_repo: AchievementRepository,
        lifestyle_repo: LifestyleRepository,
        meditation_repo: MeditationRepository
    ):
        self.achievement_repo = achievement_repo
        self.lifestyle_repo = lifestyle_repo
        self.meditation_repo = meditation_repo

    async def get_streak(self, user_id: int) -> StreakResponse:
        # Fetch all lifestyle logs for user (limit past 180 days)
        today = date.today()
        logs = await self.lifestyle_repo.get_range(user_id, today - timedelta(days=180), today)
        if not logs:
            return StreakResponse(current_streak=0, longest_streak=0, last_log_date=None)

        # Sort logs by date descending
        sorted_logs = sorted(logs, key=lambda l: l.log_date, reverse=True)
        
        # Calculate current streak
        current_streak = 0
        expected_date = today
        
        # If the user didn't log today, check if they logged yesterday
        if sorted_logs[0].log_date < today - timedelta(days=1):
            current_streak = 0
        else:
            # We iterate to count consecutive days
            last_date = None
            consec_count = 0
            for log in sorted_logs:
                if last_date is None:
                    last_date = log.log_date
                    consec_count = 1
                else:
                    diff = last_date - log.log_date
                    if diff == timedelta(days=1):
                        consec_count += 1
                        last_date = log.log_date
                    elif diff > timedelta(days=1):
                        break
            current_streak = consec_count

        # Calculate longest streak
        # Sort ascending to calculate
        logs_asc = sorted(logs, key=lambda l: l.log_date)
        longest = 0
        temp_streak = 0
        prev_date = None
        for log in logs_asc:
            if prev_date is None:
                temp_streak = 1
            else:
                diff = log.log_date - prev_date
                if diff == timedelta(days=1):
                    temp_streak += 1
                elif diff > timedelta(days=1):
                    longest = max(longest, temp_streak)
                    temp_streak = 1
            prev_date = log.log_date
        longest = max(longest, temp_streak)

        last_log_dt = datetime.combine(sorted_logs[0].log_date, datetime.min.time()) if sorted_logs else None

        return StreakResponse(
            current_streak=current_streak,
            longest_streak=longest,
            last_log_date=last_log_dt
        )

    async def check_and_unlock_achievements(self, user_id: int) -> List[Achievement]:
        unlocked = await self.achievement_repo.get_unlocked_by_user_id(user_id)
        unlocked_names = {a.badge_name for a in unlocked}

        newly_unlocked = []

        # 1. Streak badges
        streak_info = await self.get_streak(user_id)
        if streak_info.current_streak >= 3 and "3-Day Warrior" not in unlocked_names:
            badge = Achievement(
                user_id=user_id,
                badge_name="3-Day Warrior",
                badge_type="streak",
                description="Logged lifestyle habits for 3 consecutive days."
            )
            newly_unlocked.append(await self.achievement_repo.save(badge))

        if streak_info.current_streak >= 7 and "Consistency Champ" not in unlocked_names:
            badge = Achievement(
                user_id=user_id,
                badge_name="Consistency Champ",
                badge_type="streak",
                description="Logged lifestyle habits for 7 consecutive days."
            )
            newly_unlocked.append(await self.achievement_repo.save(badge))

        # 2. Activity badges (exercise)
        # Fetch lifestyle logs
        today = date.today()
        logs = await self.lifestyle_repo.get_range(user_id, today - timedelta(days=30), today)
        has_exercise_badge = any(log.exercise_minutes >= 30 for log in logs if log.exercise_minutes is not None)
        if has_exercise_badge and "Active Starter" not in unlocked_names:
            badge = Achievement(
                user_id=user_id,
                badge_name="Active Starter",
                badge_type="exercise",
                description="Completed a daily exercise session of 30 minutes or more."
            )
            newly_unlocked.append(await self.achievement_repo.save(badge))

        # 3. Meditation badges
        # Fetch meditation sessions
        from datetime import datetime, time
        start_dt = datetime.combine(today - timedelta(days=30), time.min)
        end_dt = datetime.combine(today, time.max)
        meds = await self.meditation_repo.get_range(user_id, start_dt, end_dt)
        if len(meds) > 0 and "Mindful Path" not in unlocked_names:
            badge = Achievement(
                user_id=user_id,
                badge_name="Mindful Path",
                badge_type="meditation",
                description="Successfully logged your first meditation session."
            )
            newly_unlocked.append(await self.achievement_repo.save(badge))

        # Return all unlocked achievements (previously unlocked + new)
        return unlocked + newly_unlocked
