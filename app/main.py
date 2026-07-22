import logging
import sys
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from app.api.routers.auth import router as auth_router
from app.api.routers.health import router as health_router
from app.core.config import settings
from app.exceptions.handlers import register_exception_handlers
from app.middleware.logging_middleware import LoggingMiddleware

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

# Initialize FastAPI application
app = FastAPI(
    title=settings.APP_NAME,
    description="MindGuard AI production-ready Backend Platform API",
    version="1.0.0",
    docs_url="/docs",
    redoc_url="/redoc",
    openapi_url="/openapi.json"
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

@app.get("/")
async def root():
    return {
        "message": f"Welcome to {settings.APP_NAME} API. Please navigate to /docs for Swagger UI documentation."
    }
