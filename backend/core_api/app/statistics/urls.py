from fastapi import APIRouter

import app.statistics.repo as repo

from app.logger import get_logger
from app.db_utils import get_connection
from .dtos import (
    RouteDelayResponse,
    TimestampedDelays,
    RouteDelayRequest,
    HighestDelayRequest,
)

logger = get_logger(__name__)

statRouter = APIRouter(
    prefix='/stats',
    tags=['stats'],
)


@statRouter.get('/monthly-mean')
async def get_monthly_mean() -> TimestampedDelays:
    async with get_connection() as conn:
        delays = await repo.get_monthly_mean_delays(conn)
        return TimestampedDelays(delays=delays)


@statRouter.get('/monthly-sum')
async def get_monthly_sum() -> TimestampedDelays:
    async with get_connection() as conn:
        delays = await repo.get_monthly_sum_delays(conn)
        return TimestampedDelays(delays=delays)


@statRouter.post('/highest-delay')
async def get_highest_delays(body: HighestDelayRequest) -> TimestampedDelays:
    async with get_connection() as conn:
        delays = await repo.get_daily_max_delays(
            conn,
            body.startTimestamp,
            body.endTimestamp,
        )
        return TimestampedDelays(delays=delays)


@statRouter.post('/mean-route-delay')
async def get_mean_route_delay(
    body: RouteDelayRequest,
) -> RouteDelayResponse:
    async with get_connection() as conn:
        delays = await repo.get_monthly_mean_route_delays(
            conn,
            body.route,
            body.startTimestamp,
            body.endTimestamp,
        )
    return RouteDelayResponse(
        route=body.route,
        delays=TimestampedDelays(
            delays=delays,
        ),
    )
