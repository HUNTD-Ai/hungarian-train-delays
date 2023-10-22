import React from 'react';
import TrainIcon from '../assets/icons/train.svg';
import ArrowRightIcon from '../assets/icons/arrow-right.svg';

type Props = {
  trains: Array<Train>;
  onSelectTrain: (train: Train) => void;
};

const TrainList: React.FC<Props> = ({ trains, onSelectTrain }) => {
  return (
    <div className="flex h-full w-full flex-col p-10">
      <div
        id="train-list"
        className="h-full w-full overflow-y-auto">
        <div
          id="train-list-table-header"
          className="dark:bg-textBoxBackgroundDark bg-textBoxBackgroundLight flex h-12 w-full items-center rounded-t-[10px] border border-textColor">
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
                ? 'dark:bg-textBoxBackgroundDark bg-textBoxBackgroundLight flex h-12 w-full items-center rounded-b-[10px] border border-textColor'
                : 'dark:bg-textBoxBackgroundDark bg-textBoxBackgroundLight flex h-12 w-full items-center border border-textColor'
            }>
            <div className="flex h-full w-full items-center gap-x-2 pl-2">
              <img
                src={TrainIcon}
                alt="service icon"
              />
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
              <img
                src={ArrowRightIcon}
                alt="select train"
                onClick={() => onSelectTrain(train)}
              />
            </div>
          </div>
        ))}
      </div>
      <div className="h-12 w-full"></div>
    </div>
  );
};

export default TrainList;
