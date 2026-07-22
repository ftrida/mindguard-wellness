import logging
from datetime import date, datetime
from apscheduler.schedulers.asyncio import AsyncIOScheduler
from sqlalchemy import delete
from sqlalchemy.future import select
from app.database.database import AsyncSessionLocal
from app.models.auth import User, RefreshToken, PasswordResetToken
from app.repositories.twin_repo import TwinRepository
from app.repositories.lifestyle_repo import LifestyleRepository
from app.repositories.mood_repo import MoodRepository
from app.repositories.behavior_repo import BehaviorRepository
from app.repositories.stress_repo import StressRepository
from app.repositories.journal_repo import JournalRepository
from app.repositories.focus_repo import FocusRepository
from app.services.twin_service import TwinService
from app.services.behavior_service import BehaviorService
from app.services.stress_service import StressService

logger = logging.getLogger("mindguard.scheduler")
scheduler = AsyncIOScheduler()

async def daily_twin_snapshot_job() -> None:
    logger.info("Starting daily twin snapshot background calculation...")
    today_date = date.today()
    async with AsyncSessionLocal() as db:
        # Get all users
        result = await db.execute(select(User).where(User.is_active == True))
        users = result.scalars().all()
        
        twin_service = TwinService(
            twin_repo=TwinRepository(db),
            lifestyle_repo=LifestyleRepository(db),
            mood_repo=MoodRepository(db)
        )
        
        for user in users:
            try:
                await twin_service.calculate_and_save_twin(user.id, today_date)
                logger.info(f"Successfully updated lifestyle twin for User {user.id}")
            except Exception as e:
                logger.error(f"Error calculating twin for User {user.id}: {str(e)}")
        await db.commit()

async def weekly_behavior_drift_job() -> None:
    logger.info("Starting weekly behavior drift analysis job...")
    today_date = date.today()
    async with AsyncSessionLocal() as db:
        result = await db.execute(select(User).where(User.is_active == True))
        users = result.scalars().all()
        
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
        
        for user in users:
            try:
                await behavior_service.analyze_behavior_drift(user.id, today_date)
                logger.info(f"Successfully calculated behavior drift for User {user.id}")
            except Exception as e:
                logger.error(f"Error analyzing behavior drift for User {user.id}: {str(e)}")
        await db.commit()

async def daily_stress_assessment_job() -> None:
    logger.info("Starting daily stress assessment calculation job...")
    today_date = date.today()
    async with AsyncSessionLocal() as db:
        result = await db.execute(select(User).where(User.is_active == True))
        users = result.scalars().all()
        
        stress_service = StressService(
            stress_repo=StressRepository(db),
            lifestyle_repo=LifestyleRepository(db),
            mood_repo=MoodRepository(db),
            focus_repo=FocusRepository(db),
            journal_repo=JournalRepository(db)
        )
        
        for user in users:
            try:
                await stress_service.assess_stress_likelihood(user.id, today_date)
                logger.info(f"Successfully updated stress assessment for User {user.id}")
            except Exception as e:
                logger.error(f"Error assessing stress for User {user.id}: {str(e)}")
        await db.commit()

async def expired_token_cleanup_job() -> None:
    logger.info("Running expired refresh and reset token database cleanup...")
    now = datetime.utcnow()
    async with AsyncSessionLocal() as db:
        try:
            # Delete expired refresh tokens
            rf_stmt = delete(RefreshToken).where(RefreshToken.expires_at < now)
            await db.execute(rf_stmt)
            
            # Delete expired reset tokens
            reset_stmt = delete(PasswordResetToken).where(PasswordResetToken.expires_at < now)
            await db.execute(reset_stmt)
            
            await db.commit()
            logger.info("Expired token database cleanup complete.")
        except Exception as e:
            logger.error(f"Error during expired token database cleanup: {str(e)}")
            await db.rollback()

def start_scheduler() -> None:
    # Schedule daily calculations
    scheduler.add_job(daily_twin_snapshot_job, "cron", hour=0, minute=5, id="daily_twin_snapshot")
    scheduler.add_job(daily_stress_assessment_job, "cron", hour=23, minute=50, id="daily_stress_assessment")
    scheduler.add_job(weekly_behavior_drift_job, "cron", day_of_week="sun", hour=1, minute=0, id="weekly_behavior_drift")
    scheduler.add_job(expired_token_cleanup_job, "cron", hour=2, minute=0, id="expired_token_cleanup")
    
    scheduler.start()
    logger.info("APScheduler background tasks initialized and started.")

def shutdown_scheduler() -> None:
    scheduler.shutdown()
    logger.info("APScheduler background tasks shut down.")
