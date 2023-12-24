from typing import List, Optional, Dict
from app.mav.model import PassengerJourneyPlan, PassengerJourneyPlanDetails
from bs4 import BeautifulSoup


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
    visible_rows = [
        info_cell.findParent()
        for info_cell in timetable.find_all('td', class_='info')
    ]
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
