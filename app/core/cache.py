import time
import logging
from typing import Optional, Dict, Tuple, Any

logger = logging.getLogger("mindguard.cache")

class AsyncCache:
    def __init__(self):
        # In-memory dictionary store for caching values: {key: (value, expires_at)}
        self._store: Dict[str, Tuple[Any, float]] = {}

    async def get(self, key: str) -> Optional[Any]:
        if key not in self._store:
            return None
        value, expires_at = self._store[key]
        if time.time() > expires_at:
            # Clean expired item
            del self._store[key]
            return None
        return value

    async def set(self, key: str, value: Any, expire_seconds: int = 300) -> None:
        expires_at = time.time() + expire_seconds
        self._store[key] = (value, expires_at)

    async def delete(self, key: str) -> None:
        if key in self._store:
            del self._store[key]

# Singleton cache instance
cache = AsyncCache()
