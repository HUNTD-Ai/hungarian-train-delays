import React, { useEffect, useState } from 'react';
import { Route } from '../models/models.ts';
import { useNavigate } from 'react-router-dom';

type Props = {
  routes: Array<Route>;
};

const DelayPredictionCard: React.FC<Props> = ({ routes }) => {
  const navigate = useNavigate();

  const [departureStations, setDepartureStations] = useState<Array<string>>([]);
  const [arrivalStations, setArrivalStations] = useState<Array<string>>([]);

  const [from, setFrom] = useState<string | undefined>(undefined);
  const [to, setTo] = useState<string | undefined>(undefined);
  const [date, setDate] = useState<string | undefined>(undefined);

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

  const navigateToTimetable = () => {
    if (from != null && to != null && date != null) {
      navigate(`timetable?from=${from}&to=${to}&date=${date}`);
    }
  };

  const onSelectDepartureStation = (event: {
    target: { value: React.SetStateAction<string | undefined> };
  }) => setFrom(event.target.value);

  const onSelectArrivalStation = (event: {
    target: { value: React.SetStateAction<string | undefined> };
  }) => setTo(event.target.value);

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

          <input
            name="date"
            id="date-input"
            type="date"
            placeholder="Date"
            value={date}
            onChange={event => setDate(event.target.value)}
            className="h-12 w-[calc(100vw-4.5rem)] rounded-[10px] bg-textBoxBackgroundLight px-3 text-xl text-textBoxTextColorLight dark:bg-textBoxBackgroundDark dark:text-textBoxTextColorDark sm:w-full"
          />

          <button
            type="submit"
            disabled={
              from == null ||
              from === '' ||
              to == null ||
              to === '' ||
              date == null ||
              date === ''
            }
            className="h-12 w-full rounded-[10px] bg-primaryColor text-2xl font-bold hover:bg-primaryColorHover disabled:bg-opacity-[42%] disabled:text-opacity-[42%] hover:disabled:bg-primaryColor hover:disabled:bg-opacity-[42%]"
            onClick={() => navigateToTimetable()}>
            Next
          </button>
        </div>
      </div>
    </>
  );
};

export default DelayPredictionCard;
