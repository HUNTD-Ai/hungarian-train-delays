import os
import datetime as dt

import pandas as pd

from pycaret.classification import load_model, predict_model
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel


app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=['*'],
    allow_credentials=True,
    allow_methods=['*'],
    allow_headers=['*'],
)

model_name = os.environ.get('MODEL_NAME')
model_path = model_name if model_name is not None else 'lda_delay_api'
model = load_model(model_path)


class DelayPredictionRequest(BaseModel):
    route: str
    train_number: str
    depart_time: dt.datetime


class DelayPredictionResponse(BaseModel):
    label: int
    score: float


@app.post('/predict')
def predict(data: DelayPredictionRequest):
    pred_data = transform_request_to_training_data(data)
    predictions = predict_model(model, data=pred_data)

    return DelayPredictionResponse(
        label=int(predictions['prediction_label'].iloc[0]),
        score=float(predictions['prediction_score'].iloc[0]),
    )


def transform_request_to_training_data(
    req: DelayPredictionRequest,
) -> pd.DataFrame:
    line_kind = 'GYSEV'
    if req.train_number.startswith('55'):
        line_kind = 'MAV'
    elif req.train_number.startswith('36H'):
        line_kind = 'HEV'
    tn_len = (
        len(req.train_number) - 3
        if line_kind == 'HEV'
        else len(req.train_number) - 2
    )
    return pd.DataFrame(
        [
            {
                'route': req.route,
                'tn_len': tn_len,
                'line_kind': line_kind,
                'dow': req.depart_time.weekday(),
                'month': req.depart_time.month,
            }
        ]
    )
