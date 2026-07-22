import time
from fastapi import APIRouter, Depends, status
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.sql import text
from app.api.dependencies import get_db

router = APIRouter(prefix="/health", tags=["Health Checks"])

@router.get("/live", status_code=status.HTTP_200_OK)
async def live_check():
    """
    Liveness probe. Confirms the API container is running.
    """
    return {
        "status": "healthy",
        "timestamp": time.time()
    }

@router.get("/ready", status_code=status.HTTP_200_OK)
async def ready_check(db: AsyncSession = Depends(get_db)):
    """
    Readiness probe. Checks active database connectivity.
    """
    db_healthy = False
    try:
        # Run a simple query to assert DB health
        result = await db.execute(text("SELECT 1"))
        if result.scalar() == 1:
            db_healthy = True
    except Exception:
        pass

    if db_healthy:
        return {
            "status": "ready",
            "timestamp": time.time(),
            "services": {
                "database": "connected",
                "redis": "connected_mock"
            }
        }
    else:
        return {
            "status": "unhealthy",
            "timestamp": time.time(),
            "services": {
                "database": "disconnected",
                "redis": "connected_mock"
            }
        }

@router.get("/database", status_code=status.HTTP_200_OK)
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

@router.get("/system", status_code=status.HTTP_200_OK)
async def system_check():
    """
    Returns core service performance statistics.
    """
    import os
    import psutil

    process = psutil.Process(os.getpid())
    memory_info = process.memory_info()

    return {
        "status": "online",
        "cpu_usage_percent": psutil.cpu_percent(),
        "memory_used_bytes": memory_info.rss,
        "active_threads": process.num_threads()
    }
