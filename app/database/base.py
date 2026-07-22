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
from app.models.profile import UserProfile, EmergencyContact
from app.models.lifestyle import DailyLifestyleLog
from app.models.mood import MoodEntry
from app.models.journal import JournalEntry, JournalTag
from app.models.meditation import MeditationSession
from app.models.focus import FocusSession
from app.models.notification import Notification, NotificationPreference
