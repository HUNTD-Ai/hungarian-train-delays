import datetime as dt
from typing import List
from pydantic import BaseModel


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
