import datetime as dt

from fastapi import APIRouter
from app import time_utils

from .dtos import (
    LiveTrainDataResponse,
    LiveTrainDataRequest,
    RouteDelayResponse,
    TimestampedDelays,
    TimestampedDelay,
    RouteDelayRequest,
)


statRouter = APIRouter(
    prefix='/stats',
    tags=['stats'],
)


@statRouter.post('/live')
async def get_live_train_data(
    trainRequest: LiveTrainDataRequest,
) -> LiveTrainDataResponse:
    # TODO fetch this data from MAV's api
    return LiveTrainDataResponse(
        route=trainRequest.route,
        trainNumber=trainRequest.trainNumber,
        delay=5.6,
        delayCause='Train too old',
    )


@statRouter.get('/monthly-mean')
async def get_monthly_mean() -> TimestampedDelays:
    # TODO get this data from the database
    return TimestampedDelays(
        delays=[
            TimestampedDelay(
                timestamp=time_utils.datetime_to_utc_timestamp(
                    dt.datetime(2020, i, 1)
                ),
                delay=5.2,
            )
            for i in range(12)
        ]
    )


@statRouter.get('/monthly-sum')
async def get_monthly_sum() -> TimestampedDelays:
    # TODO get this data from the database
    return TimestampedDelays(
        delays=[
            TimestampedDelay(
                timestamp=time_utils.datetime_to_utc_timestamp(
                    dt.datetime(2020, i, 1)
                ),
                delay=4400,
            )
            for i in range(12)
        ]
    )


@statRouter.get('/highest-delay')
async def get_highest_delays() -> TimestampedDelays:
    # TODO get this data from the database
    return TimestampedDelays(
        delays=[
            TimestampedDelay(
                timestamp=time_utils.datetime_to_utc_timestamp(
                    dt.datetime(2020, 9, i)
                ),
                delay=300,
            )
            for i in range(30)
        ]
    )


@statRouter.post('/mean-route-delay')
async def get_mean_route_delay(
    trainRequest: RouteDelayRequest,
) -> RouteDelayResponse:
    # TODO fetch this data from the database
    return RouteDelayResponse(
        route=trainRequest.route,
        delays=TimestampedDelays(
            delays=[
                TimestampedDelay(
                    timestamp=time_utils.datetime_to_utc_timestamp(
                        dt.datetime(2020, 9, i)
                    ),
                    delay=300,
                )
                for i in range(30)
            ]
        ),
    )
