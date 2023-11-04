import os
import asyncio

from fastapi import FastAPI

import app.statistics.repo as sr
import app.scheduler as scheduler
import app.network_utils as network

from app.db_utils import init
from .statistics.urls import statRouter

app = FastAPI()
app.include_router(statRouter)


@app.get('/')
async def root():
    return {'message': 'Hello World'}


@app.on_event('shutdown')
async def close_resources():
    await network.close_session()


# run from uvicorn
if __name__ == 'app.app':
    # db config
    db_conf = {
        'database': os.environ.get('DB_DB'),
        'user': os.environ.get('DB_USER'),
        'host': os.environ.get('DB_HOST'),
        'port': os.environ.get('DB_PORT'),
        'password': os.environ.get('DB_PW'),
    }
    for k, v in db_conf.items():
        if v is None:
            raise ValueError(f'Failed to provide db config: {k}')

    # set up scheduled tasks to update statistics
    loop = asyncio.get_running_loop()
    loop.create_task(init(db_conf, [sr.init]))

    # TODO set to false by default when moving onto k8s deployment
    main_replica = os.environ.get('MAIN_REPLICA', True)
    if main_replica:
        schlr = scheduler.Scheduler(asyncio.get_event_loop())
        tasks = sr.get_periodic_tasks()
        for name, task in tasks:
            schlr.add_task(name, task)
        schlr.run()

    # set up network client to call other services
    network.init(loop)
