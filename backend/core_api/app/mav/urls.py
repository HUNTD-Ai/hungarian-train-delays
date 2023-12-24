from fastapi import HTTPException, status, APIRouter


import app.db_utils as db
import app.network_utils as network
import app.mav.scrape_data as scraper
import app.mav.repo as mr
import app.statistics.repo as sr

from app.mav.model import (
    TimetableRequest,
    TimetableResponse,
    LiveTrainDataRequest,
    LiveTrainDataResponse,
    RoutesResponse,
)

# later prefix can be changed, now it stays for compatibility
mavRouter = APIRouter(
    prefix='/stats',
    tags=['mav'],
)


@mavRouter.post('/timetable')
async def get_timetable_data(
    timetable_request: TimetableRequest,
) -> TimetableResponse:
    res = await scraper.get_timetable_info(
        network.get_session(),
        timetable_request.route,
        timetable_request.depart_date,
    )
    if res is None:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST)
    return TimetableResponse(plans=res)


@mavRouter.post('/live')
async def get_live_train_data(
    train_request: LiveTrainDataRequest,
) -> LiveTrainDataResponse:
    train_data = await scraper.get_train_data(
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


@mavRouter.get('/routes')
async def get_routes() -> RoutesResponse:
    async with db.get_connection() as conn:
        routes = await sr.get_routes(conn)
        return RoutesResponse(routes=routes)


@mavRouter.get('/stations')
async def get_stations() -> RoutesResponse:
    async with db.get_connection() as conn:
        stations = await mr.get_stations(conn)
        return RoutesResponse(routes=stations)
