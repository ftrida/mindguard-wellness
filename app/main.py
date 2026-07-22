import logging
import sys
from contextlib import asynccontextmanager
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from app.api.routers.auth import router as auth_router
from app.api.routers.health import router as health_router
from app.api.routers.profile import router as profile_router
from app.api.routers.lifestyle import router as lifestyle_router
from app.api.routers.mood import router as mood_router
from app.api.routers.journal import router as journal_router
from app.api.routers.meditation import router as meditation_router
from app.api.routers.focus import router as focus_router
from app.api.routers.notification import router as notification_router
from app.api.routers.dashboard import router as dashboard_router
from app.api.routers.analytics import router as analytics_router
from app.api.routers.twin import router as twin_router
from app.api.routers.behavior import router as behavior_router
from app.api.routers.stress import router as stress_router
from app.api.routers.coach import router as coach_router
from app.api.routers.recommendation import router as recommendation_router
from app.api.routers.reports import router as reports_router
from app.api.routers.goals import router as goals_router
from app.api.routers.achievements import router as achievements_router

from app.core.config import settings
from app.exceptions.handlers import register_exception_handlers
from app.middleware.logging_middleware import LoggingMiddleware
from app.core.scheduler import start_scheduler, shutdown_scheduler

# Configure logging formatters and handlers
def setup_logging() -> None:
    logging_format = "%(asctime)s - %(name)s - %(levelname)s - %(message)s"
    logging.basicConfig(
        level=logging.INFO if not settings.DEBUG else logging.DEBUG,
        format=logging_format,
        handlers=[
            logging.StreamHandler(sys.stdout)
        ]
    )
    logger = logging.getLogger("mindguard")
    logger.info("Structured logging initialized.")

setup_logging()

@asynccontextmanager
async def lifespan(app: FastAPI):
    # Startup
    start_scheduler()
    yield
    # Shutdown
    shutdown_scheduler()

# Initialize FastAPI application
app = FastAPI(
    title=settings.APP_NAME,
    description="MindGuard AI production-ready Backend Platform API",
    version="1.0.0",
    docs_url="/docs",
    redoc_url="/redoc",
    openapi_url="/openapi.json",
    lifespan=lifespan
)

# Register CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.CORS_ORIGINS,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Register request logging middleware
app.add_middleware(LoggingMiddleware)

# Register custom API exceptions and global handler overrides
register_exception_handlers(app)

# Include API routers under the /api/v1 namespace prefix
app.include_router(auth_router, prefix="/api/v1")
app.include_router(health_router, prefix="/api/v1")
app.include_router(profile_router, prefix="/api/v1")
app.include_router(lifestyle_router, prefix="/api/v1")
app.include_router(mood_router, prefix="/api/v1")
app.include_router(journal_router, prefix="/api/v1")
app.include_router(meditation_router, prefix="/api/v1")
app.include_router(focus_router, prefix="/api/v1")
app.include_router(notification_router, prefix="/api/v1")
app.include_router(dashboard_router, prefix="/api/v1")
app.include_router(analytics_router, prefix="/api/v1")
app.include_router(twin_router, prefix="/api/v1")
app.include_router(behavior_router, prefix="/api/v1")
app.include_router(stress_router, prefix="/api/v1")
app.include_router(coach_router, prefix="/api/v1")
app.include_router(recommendation_router, prefix="/api/v1")
app.include_router(reports_router, prefix="/api/v1")
app.include_router(goals_router, prefix="/api/v1")
app.include_router(achievements_router, prefix="/api/v1")

@app.get("/")
async def root():
    return {
        "message": f"Welcome to {settings.APP_NAME} API. Please navigate to /docs for Swagger UI documentation."
    }
