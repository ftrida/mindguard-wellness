from sqlalchemy import Integer, ForeignKey, Text
from sqlalchemy.orm import Mapped, mapped_column, relationship
from app.database.database import Base
from app.models.auth import AuditMixin

class CoachConversationMemory(Base, AuditMixin):
    __tablename__ = "coach_conversation_memories"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, index=True)
    user_id: Mapped[int] = mapped_column(Integer, ForeignKey("users.id"), index=True, nullable=False)
    role: Mapped[str] = mapped_column(Text, nullable=False) # "user" or "assistant" or "system"
    content: Mapped[str] = mapped_column(Text, nullable=False)

    # Relationships
    user: Mapped["User"] = relationship(back_populates="coach_conversations")
