from typing import Dict, Optional, List
from datetime import date

import aiohttp

from pydantic import BaseModel
from bs4 import BeautifulSoup


import app.time_utils as time_utils
import app.logger

from app.statistics.dtos import (
    PassengerJourneyPlan,
    PassengerJourneyPlanDetails,
)


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
DELAY_BASE_URL = 'http://vonatinfo.mav-start.hu/map.aspx/getData'
INFO_BASE_URL = 'https://elvira.mav-start.hu/elvira.dll/x/uf'
WARNING_FIELD_STYLE = (
    'background-color:red;color:white;text-align:center;white-space: normal;'
)
TIMETABLE_ROW_STYLE_1 = 'tr[style*="color:#0000FF;"]'
TIMETABLE_ROW_STYLE_2 = 'tr[style*="color:#008000;"]'
LOGGER = app.logger.get_logger(__name__)

cache = Cache()


async def get_timetable_info(
    session: aiohttp.ClientSession,
    route: str,
    depart_date: date,
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


def parse_timetable_html(
    route: str, html: str
) -> Optional[List[PassengerJourneyPlan]]:
    soup = BeautifulSoup(html, 'html.parser')
    par_div = soup.find('div', class_='timetable')
    if par_div is None:
        return None
    timetable = par_div.findChild('table')
    if timetable is None:
        return None
    visible_rows = timetable.select(TIMETABLE_ROW_STYLE_1)
    visible_rows += timetable.select(TIMETABLE_ROW_STYLE_2)
    res = []
    for row in visible_rows:
        cells = row.find_all('td')
        meaningful_info = [cell.text for cell in cells[1:6]]
        meaningful_info.append(cells[-2].text)
        meaningful_info = [c.strip() for c in meaningful_info]
        meaningful_info[1] = meaningful_info[1].split(' ')[0]
        changes = 0
        try:
            changes = int(meaningful_info[2])
        except ValueError:
            pass
        journey = PassengerJourneyPlan(
            route=route,
            departure_time=meaningful_info[0].split(' ')[0],
            arrival_time=meaningful_info[1].split(' ')[0],
            changes=changes,
            duration=meaningful_info[3],
            length_km=meaningful_info[4],
            highest_class=meaningful_info[5],
            details=[],
        )
        res.append(journey)

    more_info_tables = timetable.find_all('table')
    details = [
        parse_detailed_timetable_html(table) for table in more_info_tables
    ]
    for i in range(len(res)):
        res[i].details = details[i]
    return res


def parse_detailed_timetable_html(
    soup_table: BeautifulSoup,
) -> List[PassengerJourneyPlanDetails]:
    headers_to_keep = [
        '',
        'Timetable',
        'Factual/Estimated',
        'Information',
        'Train',
    ]
    more_info_headers = soup_table.find_all('th')
    more_info_headers = [h.text.strip() for h in more_info_headers]
    more_info_headers = [
        None if h not in headers_to_keep else h for h in more_info_headers
    ]
    more_info_rows = soup_table.find_all('tr')
    details_list = []
    for i, row in enumerate(more_info_rows):
        if i == 0:
            continue   # placeholder row with no text
        cells = row.find_all('td')
        cells = [cell.text for cell in cells]
        cells = [
            cell if more_info_headers[j] is not None else None
            for j, cell in enumerate(cells)
        ]
        cells = [cell for cell in cells if cell is not None]
        train_number = cells[4].split('\xa0')[0].strip()
        try:
            train_number = str(int(train_number))
        except Exception:
            train_number = ''
        train_route = (
            ''
            if len(cells[4].strip()) == 0
            else cells[4].split('(')[-1].split(')')[0]
        )
        info = cells[3].strip()
        info = info if info != 'későbbi továbbutazás' else 'later departure'
        details = {
            'arrival': len(train_number) == 0,
            'station': cells[0],
            'planned_time': cells[1],
            'actual_time': cells[2].strip(),
            'info': info,
            'train_route': train_route.strip().replace('\xa0', ' '),
            'train_number': train_number,
        }
        details_list.append(details)
    last_dep: Dict = {}
    for i, detail in enumerate(details_list):
        if detail['arrival']:
            details_list[i - 1] = {
                **last_dep,
                'to_station': detail['station'],
                'arr_info': detail['info'],
                'arr_actual_time': detail['actual_time'],
                'arr_planned_time': detail['planned_time'],
            }
            details_list[i] = None
        else:
            details_list[i] = {
                'from_station': detail['station'],
                'dep_info': detail['info'],
                'dep_planned_time': detail['planned_time'],
                'dep_actual_time': detail['actual_time'],
                'train_number': detail['train_number'],
            }
            last_dep = details_list[i]

    return [
        PassengerJourneyPlanDetails(**d) for d in details_list if d is not None
    ]


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
