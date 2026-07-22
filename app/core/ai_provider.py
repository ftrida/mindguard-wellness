import logging
from abc import ABC, abstractmethod
from typing import List, Dict, Any
from app.core.config import settings

logger = logging.getLogger("mindguard.ai_provider")

class BaseAIProvider(ABC):
    @abstractmethod
    async def generate_response(self, prompt: str, history: List[Dict[str, str]], context: Dict[str, Any]) -> str:
        """
        Generates response given user prompt, chat history list of dicts, and current user context dict.
        """
        pass

class MockAIProvider(BaseAIProvider):
    async def generate_response(self, prompt: str, history: List[Dict[str, str]], context: Dict[str, Any]) -> str:
        # Simple template rule-based mock responses
        prompt_lower = prompt.lower()
        if "stress" in prompt_lower or "anxious" in prompt_lower:
            return "Based on your Digital Twin logs, your stress levels are elevated. Try a short 10-minute meditation."
        if "sleep" in prompt_lower:
            return f"Your sleep baseline is {context.get('twin_baselines', {}).get('sleep_hours', 7.5)} hours. Try setting a standard bedtime."
        return "I've reviewed your Digital Twin metrics. Your habits look stable today. What wellness goals can I support you with?"

class GeminiAIProvider(BaseAIProvider):
    async def generate_response(self, prompt: str, history: List[Dict[str, str]], context: Dict[str, Any]) -> str:
        logger.info("Calling Google Gemini GenAI SDK...")
        # ----------------------------------------------------------------------
        # PLACEHOLDER FOR LIVE GEMINI CALL:
        # import google.generativeai as genai
        # genai.configure(api_key=settings.GEMINI_API_KEY)
        # model = genai.GenerativeModel("gemini-1.5-flash")
        # system_instruction = f"You are MindGuard's AI Wellness Coach. Context: {context}"
        # response = await model.generate_content_async(prompt)
        # return response.text
        # ----------------------------------------------------------------------
        return "[Gemini AI Adaptor Stub] Your Digital Twin baselines show healthy bounds."

class OpenAIAIProvider(BaseAIProvider):
    async def generate_response(self, prompt: str, history: List[Dict[str, str]], context: Dict[str, Any]) -> str:
        logger.info("Calling OpenAI ChatCompletion API...")
        return "[OpenAI ChatCompletion Adaptor Stub] Focused and rested habits are recommended."

class ClaudeAIProvider(BaseAIProvider):
    async def generate_response(self, prompt: str, history: List[Dict[str, str]], context: Dict[str, Any]) -> str:
        logger.info("Calling Anthropic Claude API...")
        return "[Claude AI Adaptor Stub] Keeping consistent screen time supports focus."

# Provider Registry Factory
class AIProviderRegistry:
    @staticmethod
    def get_provider(provider_name: str) -> BaseAIProvider:
        name = provider_name.lower()
        if name == "gemini":
            return GeminiAIProvider()
        elif name == "openai":
            return OpenAIAIProvider()
        elif name == "claude":
            return ClaudeAIProvider()
        else:
            logger.info("Defaulting to MockAIProvider wellness engine.")
            return MockAIProvider()

# Registry dependency helper
def get_ai_provider() -> BaseAIProvider:
    # Read provider name from config (defaults to 'mock')
    provider_name = getattr(settings, "AI_PROVIDER", "mock")
    return AIProviderRegistry.get_provider(provider_name)
