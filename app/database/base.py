# Import all models here so that Alembic env metadata has them in scope for migrations discovery
from app.database.database import Base
from app.models.auth import (
    User,
    Role,
    RefreshToken,
    PasswordResetToken,
    EmailVerification,
    LoginHistory,
    Device
)
