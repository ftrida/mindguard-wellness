import json
from datetime import date, datetime
from typing import Optional, List, Dict, Any
from app.repositories.coach_repo import CoachRepository
from app.services.twin_service import TwinService
from app.services.behavior_service import BehaviorService
from app.services.stress_service import StressService
from app.services.recommendation_service import RecommendationService
from app.models.coach import CoachConversationMemory
from app.schemas.coach import CoachAdviceResponse

from app.core.ai_provider import BaseAIProvider, get_ai_provider

class CoachService:
    def __init__(
        self,
        coach_repo: CoachRepository,
        twin_service: TwinService,
        behavior_service: BehaviorService,
        stress_service: StressService,
        recommendation_service: RecommendationService,
        ai_provider: Optional[BaseAIProvider] = None
    ):
        self.coach_repo = coach_repo
        self.twin_service = twin_service
        self.behavior_service = behavior_service
        self.stress_service = stress_service
        self.recommendation_service = recommendation_service
        self.ai_provider = ai_provider or get_ai_provider()

    async def get_history(self, user_id: int, limit: int = 20) -> List[CoachConversationMemory]:
        return await self.coach_repo.get_memory_by_user_id(user_id, limit)

    async def clear_conversation(self, user_id: int) -> None:
        await self.coach_repo.clear_memory_by_user_id(user_id)

    async def build_user_context(self, user_id: int) -> Dict[str, Any]:
        """
        Compiles the state context of the user from all AI submodules.
        This structured state is ready to be fed directly into an LLM system prompt.
        """
        twin = await self.twin_service.get_latest_twin(user_id)
        drift = await self.behavior_service.get_latest_analysis(user_id)
        stress = await self.stress_service.get_latest_assessment(user_id)
        recs = await self.recommendation_service.get_active_recommendations(user_id)

        context = {
            "twin_baselines": {
                "sleep_hours": twin.sleep_baseline if twin else 7.5,
                "exercise_minutes": twin.activity_baseline if twin else 30.0,
                "screen_time": twin.screen_baseline if twin else 4.0,
                "mood_score": twin.mood_baseline if twin else 7.0,
                "wellness_score": twin.wellness_score if twin else 75.0
            },
            "recent_drift": {
                "drift_score": drift.drift_score if drift else 0.0,
                "consistency_score": drift.consistency_score if drift else 100.0,
                "lifestyle_change_index": drift.lifestyle_change_index if drift else 0.0,
                "risk_indicators": json.loads(drift.risk_indicators) if drift and drift.risk_indicators else []
            },
            "stress_assessment": {
                "stress_score": stress.stress_score if stress else 0.0,
                "confidence_score": stress.confidence_score if stress else 0.5,
                "contributing_factors": json.loads(stress.contributing_factors) if stress and stress.contributing_factors else {}
            },
            "active_recommendations": [r.title for r in recs]
        }
        return context

    async def get_coaching_advice(self, user_id: int) -> CoachAdviceResponse:
        context = await self.build_user_context(user_id)
        twin = context["twin_baselines"]
        stress = context["stress_assessment"]
        drift = context["recent_drift"]

        # Default fallback coaching advice templates
        daily_advice = "Make sure to take a 5-minute movement break for every hour spent sitting."
        weekly_advice = "This week, target increasing your consistency by keeping a regular bedtime."
        monthly_advice = "Over the past month, your wellness metrics show steady progress in hydration."
        lifestyle_suggestions = ["Stay hydrated", "Aim for 8 hours sleep", "Do a 20m walk"]
        motivation_messages = ["Small steps lead to long-term health changes!", "Consistency is the key to progress."]
        goal_suggestions = ["Drink 2.5L water", "Walk 10,000 steps today"]

        # Dynamic advice based on user context
        if stress["stress_score"] > 50.0:
            daily_advice = "Your estimated stress likelihood is elevated today. Try a short 10-minute meditation or a walking session."
            lifestyle_suggestions.append("Engage in deep breathing exercises")
            motivation_messages.append("Take a deep breath. It's okay to slow down and rest.")
        elif twin["wellness_score"] > 80.0:
            daily_advice = "Your wellness score is excellent! Keep up the balanced habits you've built."
            motivation_messages.append("You are doing fantastic! Keep crushing your goals.")

        if drift["drift_score"] > 25.0:
            weekly_advice = f"Alert: We detected a significant drift ({round(drift['drift_score'], 1)}%) in your behavior metrics compared to baseline."

        return CoachAdviceResponse(
            daily_advice=daily_advice,
            weekly_advice=weekly_advice,
            monthly_advice=monthly_advice,
            lifestyle_suggestions=lifestyle_suggestions,
            motivation_messages=motivation_messages,
            goal_suggestions=goal_suggestions
        )

    async def generate_response(self, user_id: int, user_message: str) -> Dict[str, Any]:
        # Save user message to memory
        u_mem = CoachConversationMemory(
            user_id=user_id,
            role="user",
            content=user_message
        )
        await self.coach_repo.save(u_mem)

        # Build Context
        context = await self.build_user_context(user_id)
        
        # Load conversation history
        history = await self.get_history(user_id, limit=10)
        history_list = [{"role": m.role, "content": m.content} for m in history]

        # Generate response using the abstract AI provider interface
        reply = await self.ai_provider.generate_response(user_message, history_list, context)

        # Save assistant message to memory
        a_mem = CoachConversationMemory(
            user_id=user_id,
            role="assistant",
            content=reply
        )
        await self.coach_repo.save(a_mem)

        # Reload full memory to return in response
        memory_list = await self.get_history(user_id, limit=20)

        return {
            "reply": reply,
            "memory": memory_list,
            "context_used": context
        }
