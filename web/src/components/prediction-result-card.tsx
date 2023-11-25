import { PredictionResult } from '../models/models.ts';
import React from 'react';
import {
  greenResultTextStyle,
  redResultTextStyle,
  yellowResultTextStyle,
} from '../constants/text-constants.ts';
import { translateDelayCauseToEnglish } from '../models/delay-causes-mapper.ts';

type Props = {
  result: PredictionResult;
};

const PredictionResultCard: React.FC<Props> = ({ result }) => {
  return (
    <div
      id="prediction-results-card"
      className="flex w-full flex-col rounded-[25px] bg-cardBackgroundLight drop-shadow-md dark:bg-cardBackgroundDark sm:w-[512px]">
      <div
        id="prediction-results-card-header"
        className="flex h-12 w-full items-center rounded-t-[25px] bg-primaryColor px-6 sm:px-12">
        <span className="text-lg font-semibold">Prediction results</span>
      </div>
      <div
        id="prediction-results-card-container"
        className="flex flex-col gap-y-[9px] px-4 py-3 sm:gap-y-[18px] sm:px-8 sm:py-6">
        <div
          id="probability-of-delays"
          className="flex flex-col">
          <span className="text-sm text-textBoxTextColorLight dark:text-textColor sm:text-lg">
            Experiencing delays
          </span>
          <span
            className={
              result.delay.confidenceLevel <= 0.8
                ? yellowResultTextStyle
                : result.delay.label === 0
                ? greenResultTextStyle
                : redResultTextStyle
            }>
            {result.delay.label === 0
              ? 'Likely less than 5 minutes '
              : 'Likely more than 5 minutes '}
            ({(result.delay.confidenceLevel * 100).toFixed(2)} % confident)
          </span>
          <span></span>
        </div>

        {result.delayCause != null && (
          <div
            id="most-probable-casue-of-delay"
            className="flex flex-col text-textBoxTextColorLight dark:text-textColor">
            <span className="text-sm sm:text-lg">
              Most probable cause of delay
            </span>
            <span className="text-lg font-semibold sm:text-2xl">
              {translateDelayCauseToEnglish(String(result.delayCause.label))} (
              {(result.delayCause.confidenceLevel * 100).toFixed(2)} %
              confident)
            </span>
          </div>
        )}

        <div
          id="select-another-train-button-container"
          className="px-8">
          <button
            id="select-another-train-button"
            className="h-12 w-full rounded-[10px] bg-primaryColor text-lg font-bold hover:bg-primaryColorHover disabled:bg-opacity-[42%] disabled:text-opacity-[42%] hover:disabled:bg-primaryColor hover:disabled:bg-opacity-[42%] sm:text-2xl"
            onClick={() => window.location.replace('/')}>
            Select another train
          </button>
        </div>
      </div>
    </div>
  );
};

export default PredictionResultCard;
