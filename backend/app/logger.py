import logging
import os


def get_logger(name: str, file: str = 'logs.log') -> logging.Logger:
    if not os.path.exists('logs'):
        os.mkdir('logs')
    logging.root.setLevel(logging.NOTSET)
    logger = logging.getLogger(name)
    if len(logger.handlers) > 0:
        return logger
    c_handler = logging.StreamHandler()
    f_handler = logging.FileHandler(os.path.join('logs', file))
    f_handler.setLevel(logging.WARNING)
    c_handler.setLevel(logging.INFO)

    format = logging.Formatter(
        '[%(asctime)s | %(name)s | %(levelname)s] %(message)s',
        datefmt='%Y-%m-%d %H:%M:%S',
    )
    c_handler.setFormatter(format)
    f_handler.setFormatter(format)

    logger.addHandler(c_handler)
    logger.addHandler(f_handler)

    return logger
