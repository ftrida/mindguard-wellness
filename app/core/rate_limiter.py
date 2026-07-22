import time
from typing import Dict, List
from fastapi import Request, HTTPException, status

class InMemoryRateLimiter:
    def __init__(self, limit: int = 60, window_seconds: int = 60):
        self.limit = limit
        self.window_seconds = window_seconds
        self.requests: Dict[str, List[float]] = {}

    def is_allowed(self, client_ip: str) -> bool:
        now = time.time()
        # Initialize client records if missing
        if client_ip not in self.requests:
            self.requests[client_ip] = []
        
        # Clean timestamps older than sliding window
        self.requests[client_ip] = [t for t in self.requests[client_ip] if now - t < self.window_seconds]

        # Check limit
        if len(self.requests[client_ip]) >= self.limit:
            return False
        
        # Record current request time
        self.requests[client_ip].append(now)
        return True

# Singleton instance
global_rate_limiter = InMemoryRateLimiter(limit=100, window_seconds=60)

async def rate_limit_dependency(request: Request):
    client_ip = request.client.host if request.client else "unknown"
    if not global_rate_limiter.is_allowed(client_ip):
        raise HTTPException(
            status_code=status.HTTP_429_TOO_MANY_REQUESTS,
            detail="Rate limit exceeded. Please try again later."
        )
