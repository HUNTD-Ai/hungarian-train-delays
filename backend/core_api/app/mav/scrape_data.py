import asyncio
from typing import Optional, List, Tuple
import datetime as dt

import aiohttp
from bs4 import BeautifulSoup

from app import logger
from app import network_utils
from app.db_utils import get_connection
from app.mav.model import TrainData, PassengerJourneyPlan
from app.mav.repo import store_train_data, get_cached_data, cache_train_data
from app.mav.parse_data import parse_timetable_html
from app.scheduler import PeriodicTask, Task


LOGGER = logger.get_logger(__name__)
TRAIN_DELAY_MAX_MINUTES = 5

DELAY_BASE_URL = 'http://vonatinfo.mav-start.hu/map.aspx/getData'
INFO_BASE_URL = 'https://elvira.mav-start.hu/elvira.dll/x/uf'

WARNING_FIELD_STYLE = (
    'background-color:red;color:white;text-align:center;white-space: normal;'
)


async def _cache_train_data() -> None:
    session = network_utils.get_session()
    await asyncio.wait_for(get_all_train_data(session), timeout=5)


def get_periodic_tasks() -> List[Tuple[str, Task]]:
    return [
        # fills cache with data, so that first request is fast
        (
            'prefetch-train-data',
            PeriodicTask(_cache_train_data, period=dt.timedelta(seconds=30)),
        ),
        # stores train data every 2 minutes
        (
            'scrape-train-data',
            PeriodicTask(scrape_train_data, period=dt.timedelta(minutes=2)),
        ),
    ]


async def scrape_train_data() -> None:
    session = network_utils.get_session()
    train_delays = await get_all_train_data(session)
    LOGGER.info(f'Scraped {len(train_delays)} trains')
    delay_cause_tasks = {
        t.elvira_id: get_delay_cause(session, t.elvira_id, t.train_number)
        for t in train_delays
        if t.delay > TRAIN_DELAY_MAX_MINUTES
    }
    LOGGER.info(f'Getting delay cause for {len(delay_cause_tasks)} trains')
    for i, td in enumerate(train_delays):
        task = delay_cause_tasks.get(td.elvira_id)
        if task is not None:
            try:
                td.delay_cause = await asyncio.wait_for(task, timeout=5)
                train_delays[i] = td
                LOGGER.debug(
                    f'Got delay cause for train {td.train_number}',
                )
            except asyncio.TimeoutError:
                LOGGER.warn(
                    f'Timeout while waiting for delay cause for train {td.train_number}',
                )
    LOGGER.info('Scraping finished')
    async with get_connection() as conn:
        await store_train_data(conn, train_data=train_delays)


async def get_timetable_info(
    session: aiohttp.ClientSession,
    route: str,
    depart_date: dt.date,
) -> Optional[List[PassengerJourneyPlan]]:
    params = {
        'd': f'{str(depart_date.year)[-2:]}.{depart_date.month}.{depart_date.day}',
        'hkn': 1,
        'language': 2,
        'i': route.split(' - ')[0],
        'e': route.split(' - ')[-1],
        'go': 'Timetable',
        '_charset_': 'UTF-8',
    }
    try:
        async with session.get(url=INFO_BASE_URL, params=params) as resp:
            text = await resp.text(encoding='latin1')
            return parse_timetable_html(route, text)
    except Exception:
        LOGGER.warn(
            f'failed to retreive timetable info for params: {params}',
            exc_info=True,
        )
        return None


async def get_train_data(
    session: aiohttp.ClientSession,
    train_number: int,
) -> Optional[TrainData]:
    train_data = await get_all_train_data(session)
    train = None
    for t in train_data:
        if t.train_number.endswith(str(train_number)):
            train = t
            break
    if train is None:
        return None
    if train.delay > TRAIN_DELAY_MAX_MINUTES:
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
            url=DELAY_BASE_URL,
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
            warning_text = style_match[-1]
            return warning_text.split(';')[-1]
    except Exception:
        LOGGER.error(
            'failed to get delay cause data',
            exc_info=True,
        )
        return None


async def get_all_train_data(
    session: aiohttp.ClientSession,
) -> List[TrainData]:
    cached = get_cached_data()
    if cached is not None:
        LOGGER.info('Cache hit')
        return cached
    LOGGER.info('Cache miss')
    try:
        async with session.post(
            url=DELAY_BASE_URL,
            json={
                'a': 'TRAINS',
                'jo': {
                    'pre': True,
                    'history': False,
                    'id': False,
                },
            },
        ) as res:
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
                LOGGER.warn('train was none')
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
            cache_train_data(train_data)
            return train_data
    except Exception:
        LOGGER.error(
            'failed to get train data',
            exc_info=True,
        )
        return []
