import json
from typing import List, Union
from pydantic import AnyHttpUrl, BeforeValidator, Field
from pydantic_settings import BaseSettings, SettingsConfigDict
from typing_extensions import Annotated

def parse_cors_origins(v: Union[str, List[str]]) -> List[str]:
    if isinstance(v, str) and not v.startswith("["):
        return [i.strip() for i in v.split(",")]
    elif isinstance(v, (list, str)):
        try:
            parsed = json.loads(v) if isinstance(v, str) else v
            if isinstance(parsed, list):
                return [str(origin) for origin in parsed]
        except Exception:
            pass
    return []

class Settings(BaseSettings):
    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore"
    )

    APP_ENV: str = "development"
    APP_NAME: str = "MindGuard AI"
    DEBUG: bool = True

    # Server Port settings
    HOST: str = "0.0.0.0"
    PORT: int = 10000

    # Database Configuration
    DATABASE_URL: str = "mysql+aiomysql://dbadmin:password@localhost:3306/mindguard"

    # Redis Configuration
    REDIS_URL: str = "redis://localhost:6379/0"

    # Security Keys
    JWT_SECRET_KEY: str = "default_access_secret_key_change_me_in_prod"
    JWT_REFRESH_SECRET_KEY: str = "default_refresh_secret_key_change_me_in_prod"
    ACCESS_TOKEN_EXPIRE_MINUTES: int = 30
    REFRESH_TOKEN_EXPIRE_DAYS: int = 7
    ALGORITHM: str = "HS256"

    # CORS configuration
    CORS_ORIGINS: Annotated[List[str], BeforeValidator(parse_cors_origins)] = []

    # SMTP Mail settings
    SMTP_HOST: str = "smtp.gmail.com"
    SMTP_PORT: int = 587
    SMTP_USERNAME: str = ""
    SMTP_PASSWORD: str = ""
    SMTP_FROM_EMAIL: str = ""
    SMTP_FROM_NAME: str = "MindGuard AI Team"

    # AI Configuration
    AI_PROVIDER: str = "gemini"
    GEMINI_API_KEY: str = "AQ.Ab8RN6K468zq7B96y4rfVpTamJtgtjUOgQT-oinJtPjp-cAdTQ"

settings = Settings()
