from typing import Optional, List
from app.repositories.recommendation_repo import RecommendationRepository
from app.repositories.lifestyle_repo import LifestyleRepository
from app.repositories.mood_repo import MoodRepository
from app.models.recommendation import Recommendation
from datetime import date, datetime, timedelta

class RecommendationService:
    def __init__(
        self,
        rec_repo: RecommendationRepository,
        lifestyle_repo: LifestyleRepository,
        mood_repo: MoodRepository
    ):
        self.rec_repo = rec_repo
        self.lifestyle_repo = lifestyle_repo
        self.mood_repo = mood_repo

    async def get_active_recommendations(self, user_id: int) -> List[Recommendation]:
        recs = await self.rec_repo.get_active_by_user_id(user_id)
        if not recs:
            # Generate new recommendation cards dynamically
            recs = await self.generate_recommendations(user_id)
        return recs

    async def read_recommendation(self, rec_id: int) -> Optional[Recommendation]:
        rec = await self.rec_repo.get_by_id(rec_id)
        if rec:
            rec.is_read = True
            return await self.rec_repo.save(rec)
        return None

    async def complete_recommendation(self, rec_id: int) -> Optional[Recommendation]:
        rec = await self.rec_repo.get_by_id(rec_id)
        if rec:
            rec.is_completed = True
            return await self.rec_repo.save(rec)
        return None

    async def generate_recommendations(self, user_id: int) -> List[Recommendation]:
        today_date = date.today()
        # Fetch lifestyle logs for past 7 days to assess need
        logs = await self.lifestyle_repo.get_range(user_id, today_date - timedelta(days=7), today_date)
        
        sleep_avg = 7.5
        water_avg = 2.0
        exercise_avg = 30.0
        
        if logs:
            sleep_vals = [log.sleep_hours for log in logs if log.sleep_hours is not None]
            water_vals = [log.water_intake for log in logs if log.water_intake is not None]
            exercise_vals = [log.exercise_minutes for log in logs if log.exercise_minutes is not None]
            if sleep_vals:
                sleep_avg = sum(sleep_vals) / len(sleep_vals)
            if water_vals:
                water_avg = sum(water_vals) / len(water_vals)
            if exercise_vals:
                exercise_avg = sum(exercise_vals) / len(exercise_vals)

        # Fetch mood score for past 7 days
        from datetime import datetime, time
        start_dt = datetime.combine(today_date - timedelta(days=7), time.min)
        end_dt = datetime.combine(today_date, time.max)
        moods = await self.mood_repo.get_range(user_id, start_dt, end_dt)
        mood_avg = sum(m.mood_score for m in moods) / len(moods) if moods else 7.0

        generated_recs = []

        # Check Sleep Needs (Sleep category)
        if sleep_avg < 7.0:
            rec1 = Recommendation(
                user_id=user_id,
                category="sleep",
                title="Optimize Your Sleep Schedule",
                content="Your sleep average is below 7 hours. Try to set a strict bedtime and limit caffeine after 2 PM.",
                priority_score=8.5
            )
            generated_recs.append(await self.rec_repo.save(rec1))

        # Check Hydration (Water category)
        if water_avg < 2.0:
            rec2 = Recommendation(
                user_id=user_id,
                category="water",
                title="Increase Water Intake",
                content="Hydration supports cognitive clarity. Keep a glass of water nearby and target 2.5 liters today.",
                priority_score=7.0
            )
            generated_recs.append(await self.rec_repo.save(rec2))

        # Check Activity (Exercise category)
        if exercise_avg < 20.0:
            rec3 = Recommendation(
                user_id=user_id,
                category="exercise",
                title="Add a 15-Minute Cardio Walk",
                content="Your activity level is low. Doing a simple walking session releases endorphins that reduce anxiety.",
                priority_score=6.5
            )
            generated_recs.append(await self.rec_repo.save(rec3))

        # Check Mood Needs (Mood category)
        if mood_avg < 6.0:
            rec4 = Recommendation(
                user_id=user_id,
                category="mood",
                title="Mindfulness & Breathing Pause",
                content="With mood scores lower than usual, scheduling a 5-minute silent meditation can help ground you.",
                priority_score=9.0
            )
            generated_recs.append(await self.rec_repo.save(rec4))

        # Standard baseline card if no specific deficits
        if not generated_recs:
            rec_default = Recommendation(
                user_id=user_id,
                category="lifestyle",
                title="Habit Maintenance Challenge",
                content="Your lifestyle markers are well balanced! Try to maintain this streak for 3 more days.",
                priority_score=5.0
            )
            generated_recs.append(await self.rec_repo.save(rec_default))

        # Return sorted by priority
        generated_recs.sort(key=lambda r: r.priority_score, reverse=True)
        return generated_recs
