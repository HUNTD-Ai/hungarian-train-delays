from typing import Dict, Optional, List

import aiohttp

from pydantic import BaseModel
from bs4 import BeautifulSoup

import app.network_utils as network
import app.time_utils as time_utils
import app.logger


class TrainData(BaseModel):
    train_number: str
    elvira_id: str
    route: str
    provider: str
    delay: float
    lat: float
    lon: float
    delay_cause: Optional[str] = None


class Cache(BaseModel):
    last_fetch: Optional[int] = None
    train_data: List[TrainData] = []


CACHE_MAX_AGE_SECONDS = 30
BASE_URL = 'http://vonatinfo.mav-start.hu/map.aspx/getData'
WARNING_FIELD_STYLE = (
    'background-color:red;color:white;text-align:center;white-space: normal;'
)
LOGGER = app.logger.get_logger(__name__)

cache = Cache()


async def get_train_data(
    session: aiohttp.ClientSession,
    route: str,
    train_number: int,
) -> Optional[TrainData]:
    train_data = await get_all_train_data(session)
    same_route = [train for train in train_data if train.route == route]
    train = None
    for t in same_route:
        if t.train_number.endswith(str(train_number)):
            train = t
    if train is None:
        return None
    if train.delay > 5:
        train.delay_cause = await get_delay_cause(
            session, train.elvira_id, train.train_number
        )
    return train


async def get_delay_cause(
    session: aiohttp.ClientSession,
    elvira_id: str,
    train_number: str,
) -> Optional[str]:
    try:
        async with session.post(
            url=BASE_URL,
            json={
                'a': 'TRAIN',
                'jo': {
                    'v': elvira_id,
                    'vsz': train_number,
                    'zoom': False,
                    'csakkozlekedovonat': True,
                },
            },
        ) as res:
            res_body = await res.json()
            d = res_body.get('d')
            if d is None:
                return None
            result = d.get('result')
            if result is None:
                return None
            html = result.get('html')
            if html is None:
                return None
            soup = BeautifulSoup(html, 'html.parser')
            style_match = [
                item.text
                for item in soup.find_all('th', style=WARNING_FIELD_STYLE)
            ]
            if len(style_match) == 0:
                return None
            return style_match[-1]
    except Exception:
        LOGGER.error(
            'failed to get delay cause data',
            exc_info=True,
        )
        return None


async def get_all_train_data(
    session: aiohttp.ClientSession,
) -> List[TrainData]:
    global cache
    if (
        cache.last_fetch is not None
        and cache.last_fetch
        > time_utils.current_timestamp_utc() - CACHE_MAX_AGE_SECONDS
    ):
        LOGGER.info('Cache hit')
        return cache.train_data

    LOGGER.info('Cache miss')
    try:
        async with session.post(
            url=BASE_URL,
            json={
                'a': 'TRAINS',
                'jo': {
                    'pre': True,
                    'history': False,
                    'id': False,
                },
            },
        ) as res:
            # res_body = await res.json(content_type='text/html')
            res_body = await res.json()
            d = res_body.get('d')
            if d is None:
                LOGGER.warn('d was none')
                return []
            result = d.get('result')
            if result is None:
                LOGGER.warn('result was none')
                return []
            trains = result.get('Trains')
            if trains is None:
                LOGGER.warn('trains was none')
                return []
            train = trains.get('Train')
            if train is None:
                LOGGER.warn('trainwas none')
                return []
            train_data = [
                TrainData(
                    train_number=item['@TrainNumber'],
                    elvira_id=item['@ElviraID'],
                    route=item['@Relation'],
                    provider=item['@Menetvonal'],
                    delay=item.get('@Delay', 0),
                    lat=item['@Lat'],
                    lon=item['@Lon'],
                )
                for item in train
            ]
            cache.last_fetch = time_utils.current_timestamp_utc()
            cache.train_data = train_data
            return train_data
    except Exception:
        LOGGER.error(
            'failed to get train data',
            exc_info=True,
        )
        return []
