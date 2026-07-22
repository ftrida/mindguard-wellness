from datetime import datetime
from typing import Optional, List, Any
from pydantic import BaseModel, Field, field_validator, ConfigDict

class JournalEntryBase(BaseModel):
    title: str = Field(..., min_length=1, max_length=200)
    content: str = Field(..., min_length=1)
    category: str = Field("General", max_length=100)

class JournalEntryCreate(JournalEntryBase):
    tags: List[str] = Field(default_factory=list)

    @field_validator("tags")
    @classmethod
    def clean_tags(cls, v: List[str]) -> List[str]:
        # Normalize: strip whitespace, remove empty, lowercase
        cleaned = [t.strip().lower() for t in v if t.strip()]
        return list(set(cleaned))

class JournalEntryUpdate(BaseModel):
    title: Optional[str] = Field(None, min_length=1, max_length=200)
    content: Optional[str] = Field(None, min_length=1)
    category: Optional[str] = Field(None, max_length=100)
    tags: Optional[List[str]] = None

    @field_validator("tags")
    @classmethod
    def clean_tags(cls, v: Optional[List[str]]) -> Optional[List[str]]:
        if v is None:
            return v
        cleaned = [t.strip().lower() for t in v if t.strip()]
        return list(set(cleaned))

class JournalTagResponse(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    id: int
    name: str

class JournalEntryResponse(JournalEntryBase):
    model_config = ConfigDict(from_attributes=True)

    id: int
    user_id: int
    created_at: datetime
    updated_at: datetime
    tags: List[JournalTagResponse] = Field(default_factory=list)

    @field_validator("tags", mode="before")
    @classmethod
    def resolve_secondary_tags(cls, v: Any) -> Any:
        # In SQLAlchemy many-to-many relationships, it resolves into a list of models
        return v
