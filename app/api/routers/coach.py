from typing import List
from fastapi import APIRouter, Depends, status, Response
from sqlalchemy.ext.asyncio import AsyncSession
from app.database.session import get_db
from app.api.dependencies import get_current_active_user
from app.models.auth import User
from app.repositories.coach_repo import CoachRepository
from app.repositories.twin_repo import TwinRepository
from app.repositories.lifestyle_repo import LifestyleRepository
from app.repositories.mood_repo import MoodRepository
from app.repositories.behavior_repo import BehaviorRepository
from app.repositories.stress_repo import StressRepository
from app.repositories.focus_repo import FocusRepository
from app.repositories.journal_repo import JournalRepository
from app.repositories.recommendation_repo import RecommendationRepository
from app.services.twin_service import TwinService
from app.services.behavior_service import BehaviorService
from app.services.stress_service import StressService
from app.services.recommendation_service import RecommendationService
from app.services.coach_service import CoachService
from app.schemas.coach import (
    CoachMessageCreate,
    CoachMessageResponse,
    CoachChatResponse,
    CoachAdviceResponse
)

router = APIRouter(prefix="/coach", tags=["AI Wellness Coach API"])

async def get_coach_service(db: AsyncSession = Depends(get_db)) -> CoachService:
    twin_service = TwinService(
        twin_repo=TwinRepository(db),
        lifestyle_repo=LifestyleRepository(db),
        mood_repo=MoodRepository(db)
    )
    behavior_service = BehaviorService(
        behavior_repo=BehaviorRepository(db),
        lifestyle_repo=LifestyleRepository(db),
        mood_repo=MoodRepository(db),
        twin_service=twin_service
    )
    stress_service = StressService(
        stress_repo=StressRepository(db),
        lifestyle_repo=LifestyleRepository(db),
        mood_repo=MoodRepository(db),
        focus_repo=FocusRepository(db),
        journal_repo=JournalRepository(db)
    )
    rec_service = RecommendationService(
        rec_repo=RecommendationRepository(db),
        lifestyle_repo=LifestyleRepository(db),
        mood_repo=MoodRepository(db)
    )
    return CoachService(
        coach_repo=CoachRepository(db),
        twin_service=twin_service,
        behavior_service=behavior_service,
        stress_service=stress_service,
        recommendation_service=rec_service
    )

@router.get("/conversation", response_model=List[CoachMessageResponse])
async def get_coach_conversation_history(
    current_user: User = Depends(get_current_active_user),
    service: CoachService = Depends(get_coach_service)
):
    return await service.get_history(current_user.id)

@router.post("/chat", response_model=CoachChatResponse)
async def chat_with_coach(
    payload: CoachMessageCreate,
    current_user: User = Depends(get_current_active_user),
    service: CoachService = Depends(get_coach_service)
):
    return await service.generate_response(current_user.id, payload.content)

@router.get("/advice", response_model=CoachAdviceResponse)
async def get_personalized_coach_advice(
    current_user: User = Depends(get_current_active_user),
    service: CoachService = Depends(get_coach_service)
):
    return await service.get_coaching_advice(current_user.id)

@router.delete("/conversation", status_code=status.HTTP_204_NO_CONTENT)
async def clear_coach_conversation_memory(
    current_user: User = Depends(get_current_active_user),
    service: CoachService = Depends(get_coach_service)
):
    await service.clear_conversation(current_user.id)
    return Response(status_code=status.HTTP_204_NO_CONTENT)
