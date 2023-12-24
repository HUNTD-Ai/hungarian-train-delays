from abc import ABC, abstractmethod
import asyncio
import datetime as dt
from typing import Awaitable, Dict, Callable, Optional

from app.logger import get_logger

logger = get_logger(__name__)


class Task(ABC):
    """Base class for async tasks that need to run on a schedule"""

    def __init__(self, async_func: Callable[[], Awaitable[None]]) -> None:
        self.func = async_func
        self.last_execution: Optional[dt.datetime] = None

    async def run(self) -> None:
        self.last_execution = dt.datetime.now()
        await self.func()

    @abstractmethod
    def should_run(self) -> bool:
        """Returns True if the task should be run"""


class DailyTask(Task):
    """Runs a task every day at the given hour and minute"""

    def __init__(
        self,
        hour: int,
        minute: int,
        async_func: Callable[[], Awaitable[None]],
    ) -> None:
        super().__init__(async_func)
        self.hour = hour
        self.minute = minute

    def should_run(self) -> bool:
        now = dt.datetime.now()
        if now.minute != self.minute or now.hour != self.hour:
            return False
        if self.last_execution is None:
            return True
        else:
            return self.last_execution.day != now.day


class HourlyTask(Task):
    """Runs a task every hour at the given minute and second"""

    def __init__(
        self,
        minute: int,
        second: int,
        async_func: Callable[[], Awaitable[None]],
    ) -> None:
        super().__init__(async_func)
        self.minute = minute
        self.second = second

    def should_run(self) -> bool:
        now = dt.datetime.now()
        if now.second != self.second or now.minute != self.minute:
            return False
        if self.last_execution is None:
            return True
        else:
            return self.last_execution.hour != now.hour


class MinutelyTask(Task):
    """Runs a task every minute at the given second"""

    def __init__(self, second: int, async_func: Callable[[], Awaitable[None]]):
        super().__init__(async_func)
        self.second = second

    def should_run(self) -> bool:
        now = dt.datetime.now()
        if now.second != self.second:
            return False
        if self.last_execution is None:
            return True
        else:
            return self.last_execution.minute != now.minute


class PeriodicTask(Task):
    """Runs a task after a given time has passed"""

    def __init__(
        self,
        async_func: Callable[[], Awaitable[None]],
        period: dt.timedelta,
    ) -> None:
        super().__init__(async_func)
        self.period = period

    def should_run(self) -> bool:
        if self.last_execution is None:
            return True
        now = dt.datetime.now()
        return now - self.last_execution > self.period


class Scheduler:
    """Scheduler that runs async tasks periodically"""

    def __init__(
        self,
        loop: asyncio.AbstractEventLoop,
        min_sleep_sec: int = 1,
    ):
        self.tasks: Dict[str, Task] = {}
        self.loop = loop
        self.min_sleep = min_sleep_sec

    def add_task(self, name: str, task: Task) -> None:
        self.tasks[name] = task

    def remove_task(self, name: str) -> None:
        if name in self.tasks.keys():
            self.tasks.pop(name)

    def run(self) -> None:
        logger.info('Started scheduler loop')
        tsk = self._loop()
        self.loop.create_task(tsk)

    async def _loop(self) -> None:
        while True:
            for name, task in self.tasks.items():
                logger.debug(
                    f'Task {name} considered for execution',
                )
                if task.should_run():
                    logger.info(
                        f'Task {name} started',
                    )
                    try:
                        await task.run()
                    except Exception:
                        logger.error(
                            f'Task {name} failed with exception',
                            exc_info=True,
                        )
            await asyncio.sleep(self.min_sleep)
