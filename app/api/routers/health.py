import time
import os
import psutil
from fastapi import APIRouter, Depends, status
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.sql import text
from app.api.dependencies import get_db
from app.core.config import settings
from app.core.scheduler import scheduler
from app.core.ai_provider import get_ai_provider

router = APIRouter(tags=["Health Checks"])

@router.get("/health", status_code=status.HTTP_200_OK)
async def health_check(db: AsyncSession = Depends(get_db)):
    """
    Consolidated Health check endpoint for database, scheduler, and system resources.
    """
    db_status = "disconnected"
    try:
        await db.execute(text("SELECT 1"))
        db_status = "connected"
    except Exception:
        pass

    sched_status = "stopped"
    if scheduler.running:
        sched_status = "running"

    process = psutil.Process(os.getpid())
    memory_info = process.memory_info()

    return {
        "status": "healthy" if db_status == "connected" else "unhealthy",
        "timestamp": time.time(),
        "database": db_status,
        "scheduler": sched_status,
        "system": {
            "memory_used_mb": round(memory_info.rss / (1024 * 1024), 2),
            "cpu_percent": psutil.cpu_percent(),
        },
        "version": "1.0.0"
    }

@router.get("/health/database", status_code=status.HTTP_200_OK)
async def database_check(db: AsyncSession = Depends(get_db)):
    """
    Detailed Database health verification check.
    """
    start_time = time.time()
    try:
        await db.execute(text("SELECT 1"))
        latency = (time.time() - start_time) * 1000  # latency in ms
        return {
            "status": "connected",
            "latency_ms": round(latency, 2)
        }
    except Exception as e:
        return {
            "status": "disconnected",
            "error": str(e)
        }

@router.get("/health/system", status_code=status.HTTP_200_OK)
async def system_check():
    """
    Returns core service performance statistics.
    """
    process = psutil.Process(os.getpid())
    memory_info = process.memory_info()
    return {
        "status": "online",
        "cpu_usage_percent": psutil.cpu_percent(),
        "memory_used_bytes": memory_info.rss,
        "active_threads": process.num_threads()
    }

@router.get("/health/scheduler", status_code=status.HTTP_200_OK)
async def scheduler_check():
    """
    Checks if the background job scheduler is running.
    """
    return {
        "scheduler_running": scheduler.running,
        "jobs_count": len(scheduler.get_jobs())
    }

@router.get("/health/ai", status_code=status.HTTP_200_OK)
async def ai_check():
    """
    Validates current configured AI provider integration status.
    """
    provider = get_ai_provider()
    provider_name = provider.__class__.__name__
    return {
        "status": "active",
        "configured_provider": provider_name
    }

@router.get("/version", status_code=status.HTTP_200_OK)
async def get_version():
    """
    Returns current API release version information.
    """
    return {
        "version": "1.0.0",
        "environment": settings.APP_ENV,
        "app_name": settings.APP_NAME
    }
