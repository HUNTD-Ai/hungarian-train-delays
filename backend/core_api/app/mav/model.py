import datetime as dt
from pydantic import BaseModel
from typing import Optional, List


class RoutesResponse(BaseModel):
    routes: List[str]


class TrainData(BaseModel):
    train_number: str
    elvira_id: str
    route: str
    provider: str
    delay: float
    lat: float
    lon: float
    delay_cause: Optional[str] = None


class PassengerJourneyPlanDetails(BaseModel):
    from_station: str
    to_station: str
    dep_planned_time: str
    dep_actual_time: str
    dep_info: str
    arr_planned_time: str
    arr_actual_time: str
    arr_info: str
    train_number: str


class PassengerJourneyPlan(BaseModel):
    route: str
    arrival_time: str
    departure_time: str
    changes: int
    duration: str
    length_km: Optional[str]
    highest_class: str
    details: List[PassengerJourneyPlanDetails]


class LiveTrainDataRequest(BaseModel):
    route: str
    trainNumber: int


class LiveTrainDataResponse(BaseModel):
    route: str
    trainNumber: int
    delay: float
    delayCause: Optional[str] = None


class TimetableRequest(BaseModel):
    route: str
    depart_date: dt.date


class TimetableResponse(BaseModel):
    plans: List[PassengerJourneyPlan]
