import asyncpg

from pydantic import BaseModel
from typing import List, Optional

import app.time_utils as time_utils
from app.mav.model import TrainData


class Cache(BaseModel):
    last_fetch: Optional[int] = None
    train_data: List[TrainData] = []


CACHE_MAX_AGE_SECONDS = 30
cache = Cache()


async def get_stations(conn: asyncpg.Connection) -> List[str]:
    stations = await conn.fetch('SELECT name FROM stations')
    return [s['name'] for s in stations]


async def store_train_data(
    conn: asyncpg.Connection,
    train_data: List[TrainData],
) -> None:
    await conn.executemany(
        """
        INSERT INTO train_data(
        timestamp,
        elvira_id,
        relation,
        train_number,
        lat,
        lon,
        line_kind,
        delay,
        delay_cause
        ) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9);
        """,
        [
            (
                time_utils.current_timestamp_utc(),
                t.elvira_id,
                t.route,
                t.train_number,
                t.lat,
                t.lon,
                t.provider,
                t.delay,
                t.delay_cause,
            )
            for t in train_data
        ],
    )


def get_cached_data() -> Optional[List[TrainData]]:
    global cache
    if (
        cache.last_fetch is not None
        and cache.last_fetch
        > time_utils.current_timestamp_utc() - CACHE_MAX_AGE_SECONDS
    ):
        return cache.train_data
    return None


def cache_train_data(train_data: List[TrainData]) -> None:
    global cache
    cache.last_fetch = time_utils.current_timestamp_utc()
    cache.train_data = train_data
