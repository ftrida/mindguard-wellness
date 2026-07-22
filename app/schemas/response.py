from typing import Generic, TypeVar, Optional, List
from pydantic import BaseModel

T = TypeVar("T")

class StandardResponse(BaseModel, Generic[T]):
    status: str = "success"
    request_id: Optional[str] = None
    data: Optional[T] = None
    error: Optional[dict] = None

class PaginationMetadata(BaseModel):
    page: int
    limit: int
    total_items: int
    total_pages: int

class PaginatedResponse(BaseModel, Generic[T]):
    status: str = "success"
    request_id: Optional[str] = None
    data: List[T]
    pagination: PaginationMetadata
    error: Optional[dict] = None
