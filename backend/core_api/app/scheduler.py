import asyncio
import datetime as dt
from typing import Awaitable, Dict, Callable, Optional

from app.logger import get_logger

logger = get_logger(__name__)


class DailyTask:
    def __init__(
        self,
        hour: int,
        minute: int,
        async_func: Callable[[], Awaitable[None]],
    ) -> None:
        self.hour = hour
        self.minute = minute
        self.func = async_func
        self.last_execution: Optional[dt.datetime] = None

    def was_run_today(self) -> bool:
        if self.last_execution is None:
            return False
        now = dt.datetime.now()
        return (
            self.last_execution.month == now.month
            and self.last_execution.day == now.day
        )

    async def run(self) -> None:
        self.last_execution = dt.datetime.now()
        await self.func()


class Scheduler:
    def __init__(self, loop: asyncio.AbstractEventLoop):
        self.tasks: Dict[str, DailyTask] = {}
        self.loop = loop

    def add_task(self, name: str, task: DailyTask) -> None:
        self.tasks[name] = task

    def remove_task(self, name: str) -> None:
        if name in self.tasks.keys():
            self.tasks.pop(name)

    def run(self) -> None:
        tsk = self._loop()
        self.loop.create_task(tsk)

    async def _loop(self) -> None:
        while True:
            now = dt.datetime.now()
            for name, task in self.tasks.items():
                if (
                    task.minute == now.minute
                    and task.hour == now.hour
                    and not task.was_run_today()
                ):
                    try:
                        await task.run()
                    except Exception:
                        logger.error(
                            f'Task {name} failed with exception',
                            exc_info=True,
                        )
            await asyncio.sleep(10)
