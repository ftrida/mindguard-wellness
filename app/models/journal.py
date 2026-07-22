from typing import List
from sqlalchemy import Table, Column, Integer, ForeignKey, String, Text
from sqlalchemy.orm import Mapped, mapped_column, relationship
from app.database.database import Base
from app.models.auth import AuditMixin

# Many-to-Many association table for journal entries and tags
journal_entry_tags = Table(
    "journal_entry_tags",
    Base.metadata,
    Column("journal_entry_id", Integer, ForeignKey("journal_entries.id", ondelete="CASCADE"), primary_key=True),
    Column("tag_id", Integer, ForeignKey("journal_tags.id", ondelete="CASCADE"), primary_key=True)
)

class JournalTag(Base, AuditMixin):
    __tablename__ = "journal_tags"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, index=True)
    name: Mapped[str] = mapped_column(String(50), unique=True, index=True, nullable=False)

class JournalEntry(Base, AuditMixin):
    __tablename__ = "journal_entries"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, index=True)
    user_id: Mapped[int] = mapped_column(Integer, ForeignKey("users.id"), index=True, nullable=False)
    title: Mapped[str] = mapped_column(String(200), nullable=False)
    content: Mapped[str] = mapped_column(Text, nullable=False)
    category: Mapped[str] = mapped_column(String(100), default="General", nullable=False)

    # Relationships
    user: Mapped["User"] = relationship(back_populates="journal_entries")
    tags: Mapped[List["JournalTag"]] = relationship(secondary=journal_entry_tags, backref="journal_entries_backref")
