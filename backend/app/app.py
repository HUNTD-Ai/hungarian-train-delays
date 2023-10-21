from fastapi import FastAPI

from app.db_utils import init
from .statistics.urls import statRouter

app = FastAPI()
app.include_router(statRouter)
init([])


@app.get('/')
async def root():
    return {'message': 'Hello World'}
