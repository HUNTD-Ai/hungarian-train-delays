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
