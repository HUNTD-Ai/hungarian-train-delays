import sqlite3
import os

from typing import Callable, List

from .logger import get_logger

logger = get_logger(__name__)

# everything here is boilerplate sqlite stuff
# will be changed to postgres later
# might be useful for testing

_DB_PATH = os.path.abspath('data/data.sqlite')


def dict_factory(cursor, row):
    d = {}
    for idx, col in enumerate(cursor.description):
        d[col[0]] = row[idx]
    return d


def configure_db(path: str) -> None:
    _DB_PATH = path


def init(repo_initializers: List[Callable[[sqlite3.Connection], None]]):
    conn = get_connection()
    for initializer in repo_initializers:
        initializer(conn)


def get_connection() -> sqlite3.Connection:
    conn = sqlite3.connect(_DB_PATH)
    conn.row_factory = dict_factory
    conn.execute('PRAGMA journal_mode=WAL;')
    conn.execute('PRAGMA foreign_keys=ON;')
    logger.info(f'initialized connection to db {_DB_PATH}')
    return conn
