import React from 'react';
import { TrainIcon, ArrowRightIcon } from './icons.tsx';

type Props = {
  trains: Array<Train>;
  onSelectTrain: (train: Train) => void;
};

const TrainList: React.FC<Props> = ({ trains, onSelectTrain }) => {
  return (
    <div className="flex h-full w-full flex-col overflow-hidden p-5 sm:p-10">
      <div
        id="train-list"
        className="h-full w-full overflow-y-auto text-textBoxTextColorLight dark:text-textColor">
        <div
          id="train-list-table-header"
          className="flex h-12 w-full items-center rounded-t-[10px] border border-textBoxTextColorLight bg-textBoxBackgroundLight dark:border-textBoxTextColorDark dark:bg-textBoxBackgroundDark">
          <div className="flex h-full w-full items-center pl-2">
            <span>Train number</span>
          </div>
          <div className="flex h-full w-full items-center pl-2">
            <span>Departure time</span>
          </div>
          <div className="flex h-full w-full items-center pl-2">
            <span>Arrival time</span>
          </div>
          <div className="flex h-full w-full items-center pl-2">
            <span>Travel time</span>
          </div>
        </div>
        {trains.map((train, index) => (
          <div
            key={index}
            className={
              index === trains.length - 1
                ? 'flex h-12 w-full items-center rounded-b-[10px] border border-textBoxTextColorLight bg-textBoxBackgroundLight dark:border-textBoxTextColorDark dark:bg-textBoxBackgroundDark'
                : 'flex h-12 w-full items-center border border-textBoxTextColorLight bg-textBoxBackgroundLight dark:border-textBoxTextColorDark dark:bg-textBoxBackgroundDark'
            }>
            <div className="flex h-full w-full items-center gap-x-2 pl-2">
              <TrainIcon className="text-textBoxTextColorLight dark:text-textColor" />
              <span>{train.trainNumber}</span>
            </div>
            <div className="flex h-full w-full items-center pl-2">
              <span>
                {train.departureTime.getHours()}:
                {train.departureTime.getMinutes()}
              </span>
            </div>
            <div className="flex h-full w-full items-center pl-2">
              <span>
                {train.arrivalTime.getHours()}:{train.arrivalTime.getMinutes()}
              </span>
            </div>
            <div className="flex h-full w-full items-center justify-between px-2">
              <span>
                {train.travelTime.getHours()}:{train.travelTime.getMinutes()}h
              </span>
              <ArrowRightIcon
                className="text-green-500"
                onClick={() => onSelectTrain(train)}
              />
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default TrainList;
