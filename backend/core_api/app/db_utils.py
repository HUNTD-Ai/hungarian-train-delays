import asyncpg

from contextlib import asynccontextmanager
from typing import Awaitable, Callable, List

from .logger import get_logger

logger = get_logger(__name__)

_DB_POOL = None


async def init(
    config: dict,
    repo_initializers: List[Callable[[asyncpg.Connection], Awaitable[None]]],
):
    global _DB_POOL
    if _DB_POOL is None:
        _DB_POOL = await asyncpg.create_pool(**config)
    async with get_connection() as conn:
        for initializer in repo_initializers:
            await initializer(conn)


@asynccontextmanager
async def get_connection():
    global _DB_POOL
    if _DB_POOL is None:
        raise NameError('The module was not initialized')
    conn = await _DB_POOL.acquire()
    try:
        yield conn
    finally:
        await _DB_POOL.release(conn)
