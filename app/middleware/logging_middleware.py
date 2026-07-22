import time
import uuid
import logging
from fastapi import Request, Response
from starlette.middleware.base import BaseHTTPMiddleware, RequestResponseEndpoint

logger = logging.getLogger("mindguard")

class LoggingMiddleware(BaseHTTPMiddleware):
    async def dispatch(self, request: Request, call_next: RequestResponseEndpoint) -> Response:
        # Extract or generate X-Correlation-ID
        correlation_id = request.headers.get("X-Correlation-ID")
        if not correlation_id:
            correlation_id = str(uuid.uuid4())

        # Bind correlation ID to request state for access in endpoints
        request.state.correlation_id = correlation_id

        start_time = time.time()
        logger.info(f"Incoming Request: {request.method} {request.url.path} | Correlation-ID: {correlation_id}")

        try:
            response = await call_next(request)
            duration = (time.time() - start_time) * 1000
            response.headers["X-Correlation-ID"] = correlation_id
            response.headers["X-Response-Time-Ms"] = f"{duration:.2f}"

            logger.info(
                f"Request Completed: {request.method} {request.url.path} | "
                f"Status: {response.status_code} | Duration: {duration:.2f}ms | Correlation-ID: {correlation_id}"
            )
            return response
        except Exception as e:
            duration = (time.time() - start_time) * 1000
            logger.error(
                f"Request Failed: {request.method} {request.url.path} | "
                f"Error: {str(e)} | Duration: {duration:.2f}ms | Correlation-ID: {correlation_id}"
            )
            raise
