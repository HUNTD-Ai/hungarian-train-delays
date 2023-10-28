import { useState } from 'react';
import TrainList from './train-list.tsx';
import { CloseIcon, TrainIcon, UnfoldMoreIcon } from './icons.tsx';
import { PredictionResult, Train } from '../models/models.ts';
import {
  greenResultTextStyle,
  redResultTextStyle,
  yellowResultTextStyle,
} from '../constants/text-constants.ts';

const DelayPredictionCard = () => {
  const [from, setFrom] = useState<string>('');
  const [to, setTo] = useState<string>('');
  const [date, setDate] = useState<string>('');

  const [result, setResult] = useState<PredictionResult | null>(null);

  const [popupVisible, setPopupVisible] = useState<boolean>(false);
  const [train, setTrain] = useState<Train | null>(null);

  const trains = [
    {
      trainNumber: '6020',
      serviceName: 'MÁV',
      departureTime: new Date(),
      arrivalTime: new Date(),
      travelTime: new Date(),
    },
    {
      trainNumber: '6020',
      serviceName: 'MÁV',
      departureTime: new Date(),
      arrivalTime: new Date(),
      travelTime: new Date(),
    },
    {
      trainNumber: '6020',
      serviceName: 'MÁV',
      departureTime: new Date(),
      arrivalTime: new Date(),
      travelTime: new Date(),
    },
    {
      trainNumber: '6020',
      serviceName: 'MÁV',
      departureTime: new Date(),
      arrivalTime: new Date(),
      travelTime: new Date(),
    },
    {
      trainNumber: '6020',
      serviceName: 'MÁV',
      departureTime: new Date(),
      arrivalTime: new Date(),
      travelTime: new Date(),
    },
    {
      trainNumber: '6020',
      serviceName: 'MÁV',
      departureTime: new Date(),
      arrivalTime: new Date(),
      travelTime: new Date(),
    },
    {
      trainNumber: '6020',
      serviceName: 'MÁV',
      departureTime: new Date(),
      arrivalTime: new Date(),
      travelTime: new Date(),
    },
    {
      trainNumber: '6020',
      serviceName: 'MÁV',
      departureTime: new Date(),
      arrivalTime: new Date(),
      travelTime: new Date(),
    },
    {
      trainNumber: '6020',
      serviceName: 'MÁV',
      departureTime: new Date(),
      arrivalTime: new Date(),
      travelTime: new Date(),
    },
    {
      trainNumber: '6020',
      serviceName: 'MÁV',
      departureTime: new Date(),
      arrivalTime: new Date(),
      travelTime: new Date(),
    },
    {
      trainNumber: '6020',
      serviceName: 'MÁV',
      departureTime: new Date(),
      arrivalTime: new Date(),
      travelTime: new Date(),
    },
    {
      trainNumber: '6020',
      serviceName: 'MÁV',
      departureTime: new Date(),
      arrivalTime: new Date(),
      travelTime: new Date(),
    },
    {
      trainNumber: '6020',
      serviceName: 'MÁV',
      departureTime: new Date(),
      arrivalTime: new Date(),
      travelTime: new Date(),
    },
    {
      trainNumber: '6020',
      serviceName: 'MÁV',
      departureTime: new Date(),
      arrivalTime: new Date(),
      travelTime: new Date(),
    },
    {
      trainNumber: '6020',
      serviceName: 'MÁV',
      departureTime: new Date(),
      arrivalTime: new Date(),
      travelTime: new Date(),
    },
    {
      trainNumber: '6020',
      serviceName: 'MÁV',
      departureTime: new Date(),
      arrivalTime: new Date(),
      travelTime: new Date(),
    },
    {
      trainNumber: '6020',
      serviceName: 'MÁV',
      departureTime: new Date(),
      arrivalTime: new Date(),
      travelTime: new Date(),
    },
    {
      trainNumber: '6020',
      serviceName: 'MÁV',
      departureTime: new Date(),
      arrivalTime: new Date(),
      travelTime: new Date(),
    },
  ];

  const trainSelectorStyle =
    'bg-opacity-[42%] text-opacity-[42%] bg-textBoxBackgroundLight dark:bg-textBoxBackgroundDark flex h-12 w-full items-center rounded-[10px] pl-3 pr-2';
  const disabledTrainSelectorStyle =
    'bg-textBoxBackgroundLight dark:bg-textBoxBackgroundDark flex h-12 w-full items-center rounded-[10px] pl-3 pr-2';

  return (
    <>
      {result == null && (
        <div
          id="delay-prediction-card"
          className="z-2 flex w-full flex-col rounded-[25px] bg-cardBackgroundLight drop-shadow-md dark:bg-cardBackgroundDark sm:w-[512px]">
          <div
            id="delay-prediction-card-header"
            className="flex h-12 w-full items-center rounded-t-[25px] bg-primaryColor px-6 sm:px-12">
            <span className="text-lg font-semibold">Delay prediction</span>
          </div>
          <div
            id="delay-prediction-form-container"
            className="flex flex-col gap-y-3 px-4 py-5 sm:gap-y-6 sm:px-16 sm:py-9">
            <input
              name="from"
              id="from-input"
              type="text"
              placeholder="From"
              value={from}
              onChange={event => setFrom(event.target.value)}
              className="h-12 w-full rounded-[10px] bg-textBoxBackgroundLight px-3 text-xl text-textBoxTextColorLight dark:bg-textBoxBackgroundDark dark:text-textBoxTextColorDark"
            />
            <input
              name="to"
              id="to-input"
              type="text"
              placeholder="To"
              value={to}
              onChange={event => setTo(event.target.value)}
              className="h-12 w-full rounded-[10px] bg-textBoxBackgroundLight px-3 text-xl text-textBoxTextColorLight dark:bg-textBoxBackgroundDark dark:text-textBoxTextColorDark"
            />
            <div className="flex w-full flex-col gap-x-6 gap-y-3 sm:flex-row">
              <input
                name="date"
                id="date-input"
                type="date"
                placeholder="Date"
                value={date}
                onChange={event => setDate(event.target.value)}
                className="h-12 w-[calc(100vw-4.5rem)] rounded-[10px] bg-textBoxBackgroundLight px-3 text-xl text-textBoxTextColorLight dark:bg-textBoxBackgroundDark dark:text-textBoxTextColorDark sm:w-full"
              />
              <div
                id="train-selector-button"
                className={
                  from == null ||
                  from === '' ||
                  to == null ||
                  to === '' ||
                  date == null ||
                  date === ''
                    ? disabledTrainSelectorStyle
                    : trainSelectorStyle
                }
                onClick={() => {
                  if (
                    !(
                      from == null ||
                      from === '' ||
                      to == null ||
                      to === '' ||
                      date == null ||
                      date
                    )
                  ) {
                    setPopupVisible(true);
                  }
                }}>
                {train != null && (
                  <div className="flex items-center gap-x-2 text-textBoxTextColorLight dark:text-textColor">
                    <TrainIcon />
                    <span className="text-xl text-textBoxTextColorLight dark:text-textColor">
                      {train.trainNumber}
                    </span>
                  </div>
                )}
                {train == null && (
                  <span className="text-xl text-textBoxTextColorLight dark:text-textColor">
                    Train
                  </span>
                )}
                <div className="w-full" />
                <UnfoldMoreIcon className="text-textBoxTextColorLight dark:text-textColor" />
              </div>
            </div>
            <button
              type="submit"
              disabled={
                from == null ||
                from === '' ||
                to == null ||
                to === '' ||
                date == null ||
                date === '' ||
                train == null
              }
              className="h-12 w-full rounded-[10px] bg-primaryColor text-2xl font-bold hover:bg-primaryColorHover disabled:bg-opacity-[42%] disabled:text-opacity-[42%] hover:disabled:bg-primaryColor hover:disabled:bg-opacity-[42%]"
              onClick={() =>
                setResult({
                  label: 'Vonat műszaki hibája miatti késés',
                  score: 0.2125,
                  delay: 6.4 * 60 * 1000,
                })
              }>
              I'm feeling lucky
            </button>
          </div>
        </div>
      )}

      {result != null && (
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
      )}

      {popupVisible && (
        <div
          id="train-selector-popup-container"
          className="absolute left-1/2 top-24 z-10 flex h-[80%] w-full -translate-x-1/2 px-4 sm:h-[calc(100%-8rem)] sm:w-[640px]">
          <div
            id="train-selector-popup"
            className="flex w-full flex-col rounded-[25px] bg-cardBackgroundLight drop-shadow-md dark:bg-cardBackgroundDark ">
            <div
              id="train-selector-popup-header"
              className="flex h-12 w-full flex-shrink-0 items-center justify-between rounded-t-[25px] bg-primaryColor px-4">
              <span className="text-lg font-semibold">
                Trains ({from} -{'>'} {to})
              </span>
              <CloseIcon onClick={() => setPopupVisible(false)} />
            </div>
            <TrainList
              trains={trains}
              onSelectTrain={t => {
                setTrain(t);
                setPopupVisible(false);
              }}
            />
          </div>
        </div>
      )}
    </>
  );
};

export default DelayPredictionCard;
