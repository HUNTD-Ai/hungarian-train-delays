import React, { useCallback, useEffect, useState } from 'react';
import TrainList from './train-list.tsx';
import { CloseIcon, TrainIcon, UnfoldMoreIcon } from './icons.tsx';
import { Route, Train } from '../models/models.ts';
import { TrainDelayApi } from '../apis/train-delay-api.ts';
import LoadingSpinner from './loading-spinner.tsx';

type Props = {
  routes: Array<Route>;
  onSubmit: (train: Train, date: string) => void;
};

const DelayPredictionCard: React.FC<Props> = ({ routes, onSubmit }) => {
  const [departureStations, setDepartureStations] = useState<Array<string>>([]);
  const [arrivalStations, setArrivalStations] = useState<Array<string>>([]);

  const [from, setFrom] = useState<string | undefined>(undefined);
  const [to, setTo] = useState<string | undefined>(undefined);
  const [date, setDate] = useState<string | undefined>(undefined);

  const [popupVisible, setPopupVisible] = useState<boolean>(false);
  const [trainsLoading, setTrainsLoading] = useState<boolean>(false);
  const [trains, setTrains] = useState<Array<Train>>([]);
  const [train, setTrain] = useState<Train | null>(null);

  const fetchTimeTable = useCallback(async (route: Route, date: string) => {
    setTrainsLoading(true);
    const timetable = await TrainDelayApi.getTimetable(route.value, date);
    const timetableTrains: Array<Train> = [];
    timetable?.plans?.forEach(t => {
      if (t.details.length != 0) {
        timetableTrains.push({
          route: t.route,
          trainNumber: t.details[0].train_number,
          departureTime: t.departure_time,
          travelTime: t.duration,
          arrivalTime: t.arrival_time,
        });
      }
    });
    setTrains(timetableTrains);
    setTrainsLoading(false);
  }, []);

  useEffect(() => {
    const stations = Array.from(new Set(routes.map(r => r.from)));
    stations.sort();
    setDepartureStations(stations);
  }, [routes]);

  useEffect(() => {
    const stations = routes
      .filter(r => r.from === from)
      .map(r => r.to)
      .sort();
    setArrivalStations(stations);
  }, [routes, from]);

  useEffect(() => {
    if (from != undefined && to != undefined && date != undefined) {
      const route = routes.find(r => r.from === from && r.to === to);
      if (route != undefined) {
        fetchTimeTable(route, date);
      }
    }
  }, [from, to, date, routes, fetchTimeTable]);

  const submitPredictionInputs = () => {
    if (from != null && to != null && train != null && date != null) {
      onSubmit(train, date);
    }
  };

  const onSelectDepartureStation = (event: {
    target: { value: React.SetStateAction<string | undefined> };
  }) => setFrom(event.target.value);

  const onSelectArrivalStation = (event: {
    target: { value: React.SetStateAction<string | undefined> };
  }) => setTo(event.target.value);

  const trainSelectorStyle =
    'bg-opacity-[42%] text-opacity-[42%] bg-textBoxBackgroundLight dark:bg-textBoxBackgroundDark flex h-12 w-full items-center rounded-[10px] pl-3 pr-2';
  const disabledTrainSelectorStyle =
    'bg-textBoxBackgroundLight dark:bg-textBoxBackgroundDark flex h-12 w-full items-center rounded-[10px] pl-3 pr-2';

  return (
    <>
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
          <select
            value={from}
            name="from"
            id="from-input"
            onChange={onSelectDepartureStation}
            className="h-12 w-full rounded-[10px] bg-textBoxBackgroundLight px-3 text-xl text-textBoxTextColorLight dark:bg-textBoxBackgroundDark dark:text-textBoxTextColorDark">
            <option value={undefined}>From</option>
            {departureStations.map((stationName, index) => (
              <option
                key={index}
                value={stationName}>
                {stationName}
              </option>
            ))}
          </select>
          <select
            value={to}
            name="to"
            id="to-input"
            onChange={onSelectArrivalStation}
            className="h-12 w-full rounded-[10px] bg-textBoxBackgroundLight px-3 text-xl text-textBoxTextColorLight dark:bg-textBoxBackgroundDark dark:text-textBoxTextColorDark">
            <option value={undefined}>To</option>
            {arrivalStations.map((stationName, index) => (
              <option
                key={index}
                value={stationName}>
                {stationName}
              </option>
            ))}
          </select>
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
                    date === ''
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
            onClick={() => submitPredictionInputs()}>
            I'm feeling lucky
          </button>
        </div>
      </div>
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
            {trainsLoading && (
              <div className="flex h-full w-full items-center justify-center">
                <LoadingSpinner />
              </div>
            )}
            {!trainsLoading && trains.length === 0 && (
              <div className="flex h-full w-full items-center justify-center">
                Timetable is empty
              </div>
            )}
            {!trainsLoading && trains.length > 0 && (
              <TrainList
                trains={trains}
                onSelectTrain={t => {
                  setTrain(t);
                  setPopupVisible(false);
                }}
              />
            )}
          </div>
        </div>
      )}
    </>
  );
};

export default DelayPredictionCard;
