import datetime as dt


def current_timestamp_utc_seconds() -> int:
    return int(dt.datetime.now(dt.timezone.utc).timestamp())


def datetime_to_utc_timestamp(datetime: dt.datetime, return_ms=False) -> int:
    ts = int(datetime.astimezone(dt.timezone.utc).timestamp())
    if return_ms:
        ts *= 1000
    return ts


def now() -> dt.datetime:
    return dt.datetime.now(dt.timezone.utc)


def format_timedelta_in_ms(
    before: dt.datetime,
    after: dt.datetime,
) -> str:
    return '{:.2f}'.format((after - before).total_seconds() * 1000)


def datetime_as_gmt(date: dt.datetime) -> str:
    return date.strftime('%Y-%m-%d %H:%M:%S GMT')
