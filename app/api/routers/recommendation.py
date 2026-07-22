from typing import List
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.ext.asyncio import AsyncSession
from app.database.session import get_db
from app.api.dependencies import get_current_active_user
from app.models.auth import User
from app.repositories.recommendation_repo import RecommendationRepository
from app.repositories.lifestyle_repo import LifestyleRepository
from app.repositories.mood_repo import MoodRepository
from app.services.recommendation_service import RecommendationService
from app.schemas.recommendation import RecommendationResponse

router = APIRouter(prefix="/recommendations", tags=["Recommendation Engine API"])

async def get_recommendation_service(db: AsyncSession = Depends(get_db)) -> RecommendationService:
    return RecommendationService(
        rec_repo=RecommendationRepository(db),
        lifestyle_repo=LifestyleRepository(db),
        mood_repo=MoodRepository(db)
    )

@router.get("", response_model=List[RecommendationResponse])
async def get_active_recommendations(
    current_user: User = Depends(get_current_active_user),
    service: RecommendationService = Depends(get_recommendation_service)
):
    return await service.get_active_recommendations(current_user.id)

@router.post("/{id}/read", response_model=RecommendationResponse)
async def mark_recommendation_as_read(
    id: int,
    current_user: User = Depends(get_current_active_user),
    service: RecommendationService = Depends(get_recommendation_service)
):
    rec = await service.read_recommendation(id)
    if not rec or rec.user_id != current_user.id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Recommendation not found")
    return rec

@router.post("/{id}/complete", response_model=RecommendationResponse)
async def mark_recommendation_as_completed(
    id: int,
    current_user: User = Depends(get_current_active_user),
    service: RecommendationService = Depends(get_recommendation_service)
):
    rec = await service.complete_recommendation(id)
    if not rec or rec.user_id != current_user.id:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Recommendation not found")
    return rec
