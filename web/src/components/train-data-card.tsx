import { LiveDataResponse, Train } from '../models/models.ts';
import React, { useEffect, useState } from 'react';

type Props = {
  train: Train;
  liveData: LiveDataResponse | null;
};

const TrainDataCard: React.FC<Props> = ({ train, liveData }) => {
  const [travelTime, setTravelTime] = useState<string>('');

  useEffect(() => {
    const [hours, minutes] = train.travelTime.split(':');

    if (hours === '0') {
      setTravelTime(minutes + ' min');
    } else {
      return setTravelTime(hours + ' h ' + minutes + ' min');
    }
  }, [train]);

  return (
    <div
      id="train-data-card"
      className="flex w-full flex-col rounded-[25px] bg-cardBackgroundLight drop-shadow-md dark:bg-cardBackgroundDark sm:w-[512px]">
      <div
        id="train-data-card-header"
        className="flex h-12 w-full items-center rounded-t-[25px] bg-primaryColor px-6 sm:px-12">
        <span className="text-lg font-semibold">
          Train info (
          {liveData != null
            ? 'live data is available'
            : 'live data is not available'}
          )
        </span>
      </div>
      <div
        id="train-data-container"
        className="flex flex-col gap-y-[9px] px-4 py-3 sm:gap-y-[18px] sm:px-8 sm:py-6">
        <div
          id="train-data-route"
          className="flex flex-col">
          <span className="text-sm text-textBoxTextColorLight dark:text-textColor sm:text-lg">
            Route
          </span>
          <span className="text-lg font-semibold sm:text-2xl">
            {train.route}
          </span>
        </div>

        <div
          id="train-data-train-number"
          className="flex flex-col">
          <span className="text-sm text-textBoxTextColorLight dark:text-textColor sm:text-lg">
            Train number
          </span>
          <span className="text-lg font-semibold sm:text-2xl">
            {train.trainNumber}
          </span>
        </div>

        <div
          id="train-data-train-number"
          className="flex flex-col">
          <span className="text-sm text-textBoxTextColorLight dark:text-textColor sm:text-lg">
            Departure time
          </span>
          <span className="text-lg font-semibold sm:text-2xl">
            {train.departureTime}
          </span>
        </div>

        <div
          id="train-data-train-number"
          className="flex flex-col">
          <span className="text-sm text-textBoxTextColorLight dark:text-textColor sm:text-lg">
            Arrival time
          </span>
          <span className="text-lg font-semibold sm:text-2xl">
            {train.arrivalTime}
          </span>
        </div>

        <div
          id="train-data-train-number"
          className="flex flex-col">
          <span className="text-sm text-textBoxTextColorLight dark:text-textColor sm:text-lg">
            Travel time
          </span>
          <span className="text-lg font-semibold sm:text-2xl">
            {travelTime}
          </span>
        </div>

        {liveData?.delay != null && (
          <div
            id="live-data-delay"
            className="flex flex-col">
            <span className="text-sm text-textBoxTextColorLight dark:text-textColor sm:text-lg">
              Delay (live)
            </span>
            <span className="text-lg font-semibold sm:text-2xl">
              {liveData.delay}min
            </span>
          </div>
        )}

        {liveData?.delayCause != null && (
          <div
            id="live-data-delay-cause"
            className="flex flex-col">
            <span className="text-sm text-textBoxTextColorLight dark:text-textColor sm:text-lg">
              Delay cause (live)
            </span>
            <span className="text-lg font-semibold sm:text-2xl">
              {liveData.delayCause}
            </span>
          </div>
        )}
      </div>
    </div>
  );
};

export default TrainDataCard;
