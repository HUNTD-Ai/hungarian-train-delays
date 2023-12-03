import os
import asyncpg
import datetime as dt

from typing import List, Optional, Tuple

from app.db_utils import get_connection
from app.scheduler import DailyTask
from app.time_utils import datetime_to_utc_timestamp
from app.logger import get_logger
from .dtos import TimestampedDelay

logger = get_logger(__name__)


async def init(conn: asyncpg.Connection) -> None:
    path = os.path.join(
        os.path.dirname(os.path.abspath(__file__)),
        'queries',
        'ddl.sql',
    )
    query = _read_query_from_file(path)
    if query is not None:
        await conn.execute(query)


async def get_monthly_sum_delays(
    conn: asyncpg.Connection,
) -> List[TimestampedDelay]:
    res = await conn.fetch(
        'SELECT timestamp, delay FROM monthly_sum_delays mmd;'
    )
    return [
        TimestampedDelay(
            timestamp=record.get('timestamp'),
            delay=record.get('delay'),
        )
        for record in res
    ]


async def get_monthly_mean_delays(
    conn: asyncpg.Connection,
) -> List[TimestampedDelay]:
    res = await conn.fetch(
        'SELECT timestamp, delay FROM monthly_mean_delays mmd;'
    )
    return [
        TimestampedDelay(
            timestamp=record.get('timestamp'),
            delay=record.get('delay'),
        )
        for record in res
    ]


async def get_daily_max_delays(
    conn: asyncpg.Connection,
    start_timestamp: dt.datetime,
    end_timestamp: dt.datetime,
) -> List[TimestampedDelay]:
    res = await conn.fetch(
        """
        SELECT timestamp, delay 
        FROM daily_record
        WHERE
        timestamp > $1
        AND timestamp < $2
        ORDER BY
        timestamp;
        """,
        datetime_to_utc_timestamp(start_timestamp, return_ms=True),
        datetime_to_utc_timestamp(end_timestamp, return_ms=True),
    )
    logger.info(res)
    return [
        TimestampedDelay(
            timestamp=record.get('timestamp'),
            delay=record.get('delay'),
        )
        for record in res
    ]


async def get_monthly_mean_route_delays(
    conn: asyncpg.Connection,
    route: str,
    start_timestamp: dt.datetime,
    end_timestamp: dt.datetime,
) -> List[TimestampedDelay]:
    res = await conn.fetch(
        """
        SELECT timestamp, delay 
        FROM monthly_mean_route_delays
        WHERE
        timestamp > $1
        AND timestamp < $2
        AND route = $3
        ORDER BY
        timestamp;
        """,
        datetime_to_utc_timestamp(start_timestamp, return_ms=True),
        datetime_to_utc_timestamp(end_timestamp, return_ms=True),
        route,
    )
    return [
        TimestampedDelay(
            timestamp=record.get('timestamp'),
            delay=record.get('delay'),
        )
        for record in res
    ]


async def get_routes(conn: asyncpg.Connection) -> List[str]:
    return [
        rec.get('route')
        for rec in await conn.fetch(
            """
            SELECT DISTINCT route FROM monthly_mean_route_delays;
            """
        )
    ]


async def refresh_journey_delays() -> None:
    logger.info('refreshing journey delays')
    async with get_connection() as conn:
        await conn.execute('REFRESH MATERIALIZED VIEW journey_delays;')


async def refresh_montly_sum() -> None:
    path = os.path.join(
        os.path.dirname(os.path.abspath(__file__)),
        'queries',
        'monthly-sum.sql',
    )
    query = _read_query_from_file(path)
    if query is not None:
        async with get_connection() as conn:
            await conn.execute(query)


async def refresh_montly_mean() -> None:
    path = os.path.join(
        os.path.dirname(os.path.abspath(__file__)),
        'queries',
        'monthly-mean.sql',
    )
    query = _read_query_from_file(path)
    if query is not None:
        async with get_connection() as conn:
            await conn.execute(query)


async def refresh_daily_highest() -> None:
    logger.info('refresing daily daily-highest delays')
    path = os.path.join(
        os.path.dirname(os.path.abspath(__file__)),
        'queries',
        'daily-highest.sql',
    )
    query = _read_query_from_file(path)
    if query is not None:
        async with get_connection() as conn:
            await conn.execute(query)


async def refresh_monthly_mean_route_delay() -> None:
    path = os.path.join(
        os.path.dirname(os.path.abspath(__file__)),
        'queries',
        'monthly-route-mean.sql',
    )
    query = _read_query_from_file(path)
    if query is not None:
        async with get_connection() as conn:
            await conn.execute(query)


def _read_query_from_file(path: str) -> Optional[str]:
    try:
        with open(path, 'r') as dml:
            lines = dml.readlines()
            return '\n'.join(lines)
    except FileNotFoundError:
        logger.warn(f'could not find query, looked for: {path}')
        return None


def get_periodic_tasks() -> List[Tuple[str, DailyTask]]:
    return [
        (
            'refresh_journey_delays',
            DailyTask(hour=1, minute=40, async_func=refresh_journey_delays),
        ),
        (
            'refresh_daily_highest',
            DailyTask(hour=1, minute=50, async_func=refresh_daily_highest),
        ),
        (
            'refresh_mean',
            DailyTask(hour=2, minute=0, async_func=refresh_montly_mean),
        ),
        (
            'refresh_sum',
            DailyTask(hour=2, minute=10, async_func=refresh_montly_sum),
        ),
        (
            'refresh_monthly_mean_route',
            DailyTask(
                hour=2,
                minute=20,
                async_func=refresh_monthly_mean_route_delay,
            ),
        ),
    ]
