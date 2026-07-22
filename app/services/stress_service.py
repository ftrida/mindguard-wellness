import json
from datetime import date, datetime, time
from typing import Optional, List, Dict, Any
from app.repositories.stress_repo import StressRepository
from app.repositories.lifestyle_repo import LifestyleRepository
from app.repositories.mood_repo import MoodRepository
from app.repositories.focus_repo import FocusRepository
from app.repositories.journal_repo import JournalRepository
from app.models.stress import StressLikelihood

class StressService:
    def __init__(
        self,
        stress_repo: StressRepository,
        lifestyle_repo: LifestyleRepository,
        mood_repo: MoodRepository,
        focus_repo: FocusRepository,
        journal_repo: JournalRepository
    ):
        self.stress_repo = stress_repo
        self.lifestyle_repo = lifestyle_repo
        self.mood_repo = mood_repo
        self.focus_repo = focus_repo
        self.journal_repo = journal_repo

    async def get_latest_assessment(self, user_id: int) -> Optional[StressLikelihood]:
        return await self.stress_repo.get_latest_by_user_id(user_id)

    async def get_history(self, user_id: int, start_date: date, end_date: date) -> List[StressLikelihood]:
        return await self.stress_repo.get_range(user_id, start_date, end_date)

    async def assess_stress_likelihood(self, user_id: int, assessment_date: date) -> StressLikelihood:
        # Fetch lifestyle data for today
        log = await self.lifestyle_repo.get_by_date(user_id, assessment_date)
        
        # Fetch mood data for today
        start_dt = datetime.combine(assessment_date, time.min)
        end_dt = datetime.combine(assessment_date, time.max)
        moods = await self.mood_repo.get_range(user_id, start_dt, end_dt)

        # Fetch focus sessions for today
        focus_sessions = await self.focus_repo.get_range(user_id, start_dt, end_dt)

        # Fetch journal entries for today
        journals = await self.journal_repo.get_range(user_id, start_dt, end_dt)

        # Base default parameters
        sleep = log.sleep_hours if log else 7.5
        screen = log.screen_time if log else 4.0
        exercise = log.exercise_minutes if log else 30.0
        mood = sum(m.mood_score for m in moods) / len(moods) if moods else 7.0

        # Calculations
        stress_score = 0.0
        contributing_factors = {}
        recommendations = []

        # 1. Sleep Deficit (up to 30 points)
        if sleep < 7.0:
            deficit = 7.0 - sleep
            points = min((deficit / 3.0) * 30.0, 30.0)
            stress_score += points
            contributing_factors["sleep"] = f"Sleep deficit of {round(deficit, 1)} hours"
            recommendations.append("Prioritize getting at least 7-8 hours of sleep tonight.")

        # 2. Screen Excess (up to 15 points)
        if screen > 6.0:
            excess = screen - 6.0
            points = min((excess / 6.0) * 15.0, 15.0)
            stress_score += points
            contributing_factors["screen"] = f"Excessive screen time ({round(screen, 1)} hours)"
            recommendations.append("Reduce screen exposure, especially 1 hour before bedtime.")

        # 3. Exercise Deficit (up to 15 points)
        if exercise < 20.0:
            deficit = 20.0 - exercise
            points = min((deficit / 20.0) * 15.0, 15.0)
            stress_score += points
            contributing_factors["exercise"] = "Inadequate physical activity today"
            recommendations.append("Incorporate a short 15-20 minute walk to relieve muscle tension.")

        # 4. Mood Factor (up to 20 points)
        if mood < 6.0:
            deficit = 6.0 - mood
            points = min((deficit / 5.0) * 20.0, 20.0)
            stress_score += points
            contributing_factors["mood"] = f"Lower relative mood rating ({round(mood, 1)}/10)"
            recommendations.append("Take a break and engage in a calming hobby or talk to a friend.")

        # 5. Focus Fatigue (up to 10 points)
        if focus_sessions:
            completed = sum(s.completed_sessions_count for s in focus_sessions)
            if completed >= 4:
                stress_score += 10.0
                contributing_factors["focus"] = f"High mental focus load ({completed} completed pomodoro cycles)"
                recommendations.append("Take a longer break (20-30 mins) to prevent cognitive fatigue.")

        # 6. Journal Keywords / Sentiment (up to 10 points)
        stress_keywords = ["stressed", "anxious", "overwhelmed", "sad", "worried", "exhausted", "tired", "angry"]
        keyword_hits = 0
        if journals:
            for j in journals:
                content_lower = j.content.lower()
                for kw in stress_keywords:
                    if kw in content_lower:
                        keyword_hits += 1
            if keyword_hits > 0:
                points = min(keyword_hits * 2.5, 10.0)
                stress_score += points
                contributing_factors["journal"] = "Stress-related keywords detected in journal logs"
                recommendations.append("Try a guided deep breathing or mindfulness session to center yourself.")

        # Confidence Score: Based on completeness of logged indicators
        data_points = 0
        if log: data_points += 1
        if moods: data_points += 1
        if focus_sessions: data_points += 1
        if journals: data_points += 1
        confidence_score = max(0.2, data_points / 4.0)

        # Defaults
        if not contributing_factors:
            contributing_factors["general"] = "Lifestyle parameters are within healthy thresholds."
            recommendations.append("Maintain your current healthy routines!")

        stress_score = min(max(stress_score, 0.0), 100.0)

        assessment = StressLikelihood(
            user_id=user_id,
            assessment_date=assessment_date,
            stress_score=round(stress_score, 2),
            confidence_score=round(confidence_score, 2),
            contributing_factors=json.dumps(contributing_factors),
            recommendations=json.dumps(recommendations)
        )
        return await self.stress_repo.save(assessment)
