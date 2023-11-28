import pandas as pd
from pycaret.classification import load_model, predict_model
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import datetime as dt
import os

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
model_path = (
    model_name if model_name is not None else 'lr_cause_model_undersampled'
)

model = load_model(model_path)


class CausePredictionRequest(BaseModel):
    route: str
    train_number: str
    depart_time: dt.datetime


class CausePredictionResponse(BaseModel):
    label: str
    score: float


@app.post('/predict')
def predict(data: CausePredictionRequest):
    pred_data = transform_request_to_training_data(data)
    predictions = predict_model(model, data=pred_data)

    return CausePredictionResponse(
        label=predictions['prediction_label'].iloc[0],
        score=float(predictions['prediction_score'].iloc[0]),
    )


def transform_request_to_training_data(
    req: CausePredictionRequest,
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
