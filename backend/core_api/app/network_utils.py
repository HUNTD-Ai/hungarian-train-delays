import aiohttp

from asyncio import AbstractEventLoop

from .logger import get_logger

logger = get_logger(__name__)

_SESSION = None


def init(loop: AbstractEventLoop):
    global _SESSION
    if _SESSION is None:
        headers = {
            'User-Agent': 'HUNDTD.ai',
            'Content-Type': 'application/json; charset=UTF-8',
        }
        _SESSION = aiohttp.ClientSession(
            headers=headers,
            loop=loop,
        )


def get_session() -> aiohttp.ClientSession:
    global _SESSION
    if _SESSION is None:
        raise NameError('The module was not initialized')
    return _SESSION


async def close_session() -> None:
    global _SESSION
    if _SESSION is not None:
        await _SESSION.close()
