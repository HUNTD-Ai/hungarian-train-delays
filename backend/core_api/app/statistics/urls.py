from fastapi import APIRouter, HTTPException, status

import app.statistics.repo as repo
import app.network_utils as network
import app.statistics.mav_api as mav_api

from app.logger import get_logger
from app.db_utils import get_connection
from .dtos import (
    LiveTrainDataResponse,
    LiveTrainDataRequest,
    TimetableRequest,
    RouteDelayResponse,
    RoutesResponse,
    TimestampedDelays,
    RouteDelayRequest,
    HighestDelayRequest,
    TimetableResponse,
)

logger = get_logger(__name__)

statRouter = APIRouter(
    prefix='/stats',
    tags=['stats'],
)


@statRouter.post('/timetable')
async def get_timetable_data(
    timetable_request: TimetableRequest,
) -> TimetableResponse:
    res = await mav_api.get_timetable_info(
        network.get_session(),
        timetable_request.route,
        timetable_request.depart_date,
    )
    if res is None:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST)
    return TimetableResponse(plans=res)


@statRouter.post('/live')
async def get_live_train_data(
    train_request: LiveTrainDataRequest,
) -> LiveTrainDataResponse:
    train_data = await mav_api.get_train_data(
        network.get_session(),
        train_request.trainNumber,
    )
    if train_data is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail='Could not find train with supplied data',
        )
    return LiveTrainDataResponse(
        route=train_data.route,
        trainNumber=train_request.trainNumber,
        delay=train_data.delay,
        delayCause=train_data.delay_cause,
    )


@statRouter.get('/routes')
async def get_routes() -> RoutesResponse:
    async with get_connection() as conn:
        routes = await repo.get_routes(conn)
        return RoutesResponse(routes=routes)


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
