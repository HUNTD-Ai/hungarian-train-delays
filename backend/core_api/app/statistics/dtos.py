import datetime as dt
from typing import Optional, List
from pydantic import BaseModel


class LiveTrainDataRequest(BaseModel):
    route: str
    trainNumber: int


class LiveTrainDataResponse(BaseModel):
    route: str
    trainNumber: int
    delay: float
    delayCause: Optional[str] = None


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


class TimetableRequest(BaseModel):
    route: str
    depart_date: dt.date


class TimetableResponse(BaseModel):
    plans: List[PassengerJourneyPlan]


class TimestampedDelay(BaseModel):
    timestamp: int
    delay: float


class TimestampedDelays(BaseModel):
    delays: List[TimestampedDelay]


class RouteDelayRequest(BaseModel):
    route: str
    startTimestamp: dt.datetime
    endTimestamp: dt.datetime


class RouteDelayResponse(BaseModel):
    route: str
    delays: TimestampedDelays


class HighestDelayRequest(BaseModel):
    startTimestamp: dt.datetime
    endTimestamp: dt.datetime


class RoutesResponse(BaseModel):
    routes: List[str]
