import {
  greenResultTextStyle,
  redResultTextStyle,
  yellowResultTextStyle,
} from '../constants/text-constants.ts';
import { PredictionResult } from '../models/models.ts';
import React from 'react';

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
            Probability of experiencing delays
          </span>
          <span
            className={
              result.score <= 50
                ? greenResultTextStyle
                : result.score <= 80
                ? yellowResultTextStyle
                : redResultTextStyle
            }>
            {result.score} %
          </span>
        </div>

        <div
          id="most-probable-casue-of-delay"
          className="flex flex-col text-textBoxTextColorLight dark:text-textColor">
          <span className="text-sm sm:text-lg">
            Most probable cause of delay
          </span>
          <span className="text-lg font-semibold sm:text-2xl">
            {result.label}
          </span>
        </div>

        <div
          id="predicted delay"
          className="flex flex-col text-textBoxTextColorLight dark:text-textColor">
          <span className="text-sm sm:text-lg">Predicted delay</span>
          <span
            className={
              result.delay <= 5 * 60 * 1000
                ? greenResultTextStyle
                : result.delay <= 10 * 60 * 1000
                ? yellowResultTextStyle
                : redResultTextStyle
            }>
            5min 9s
          </span>
        </div>

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
