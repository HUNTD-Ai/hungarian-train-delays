import { useState } from 'react';
import TrainIcon from '../assets/icons/train.svg';
import UnfoldMoreIcon from '../assets/icons/unfold-more.svg';
import CloseIcon from '../assets/icons/close.svg';
import TrainList from './train-list.tsx';

const DelayPredictionCard = () => {
  const [from, setFrom] = useState<string>('Budapest');
  const [to, setTo] = useState<string>('Debrecen');
  const [date, setDate] = useState<string>('');

  const [popupVisible, setPopupVisible] = useState<boolean>(true);
  const [train, setTrain] = useState<Train | null>({
    trainNumber: '6020',
    serviceName: 'MÁV',
    departureTime: new Date('02:38'),
    arrivalTime: new Date('05:52'),
    travelTime: new Date('3:15'),
  });

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
      <div
        id="delay-prediction-card"
        className="bg-cardBackgroundLight dark:bg-cardBackgroundDark flex w-[512px] flex-col rounded-[25px] drop-shadow-md">
        <div
          id="delay-prediction-card-header"
          className="flex h-12 w-full items-center rounded-t-[25px] bg-primaryColor px-12">
          <span className="text-lg font-semibold">Delay prediction</span>
        </div>
        <div
          id="delay-prediction-form-container"
          className="flex flex-col gap-y-6 px-16 py-9">
          <input
            name="from"
            id="from-input"
            type="text"
            placeholder="From"
            value={from}
            onChange={event => setFrom(event.target.value)}
            className="bg-textBoxBackgroundLight dark:bg-textBoxBackgroundDark text-textBoxTextColorLight dark:text-textBoxTextColorDark h-12 w-full rounded-[10px] px-3 text-xl"
          />
          <input
            name="to"
            id="to-input"
            type="text"
            placeholder="To"
            value={to}
            onChange={event => setTo(event.target.value)}
            className="bg-textBoxBackgroundLight dark:bg-textBoxBackgroundDark text-textBoxTextColorLight dark:text-textBoxTextColorDark h-12 w-full rounded-[10px] px-3 text-xl"
          />
          <div className="flex w-full gap-x-6">
            <input
              name="date"
              id="date-input"
              type="date"
              placeholder="Date"
              value={date}
              onChange={event => setDate(event.target.value)}
              className="bg-textBoxBackgroundLight dark:bg-textBoxBackgroundDark text-textBoxTextColorLight dark:text-textBoxTextColorDark h-12 w-full rounded-[10px] px-3 text-xl"
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
              onClick={() => setPopupVisible(true)}>
              {train != null && (
                <div className="flex items-center gap-x-2">
                  <img
                    src={TrainIcon}
                    alt="service icon"
                  />
                  <span className="text-xl">{train.trainNumber}</span>
                </div>
              )}
              {train == null && <span className="text-xl">Train</span>}
              <div className="w-full" />
              <img
                src={UnfoldMoreIcon}
                alt="open train selector icon"
              />
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
            className="h-12 w-full rounded-[10px] bg-primaryColor text-2xl font-bold hover:bg-primaryColorHover disabled:bg-opacity-[42%] disabled:text-opacity-[42%] hover:disabled:bg-primaryColor hover:disabled:bg-opacity-[42%]">
            I'm feeling lucky
          </button>
        </div>
      </div>
      {popupVisible && (
        <div
          id="train-selector-popup"
          className="bg-cardBackgroundLight dark:bg-cardBackgroundDark absolute left-1/2 top-24 z-10 flex h-[calc(100%-8rem)] w-[640px] -translate-x-1/2 flex-col rounded-[25px] drop-shadow-md">
          <div
            id="train-selector-popup-header"
            className="flex h-12 w-full flex-shrink-0 items-center justify-between rounded-t-[25px] bg-primaryColor px-4">
            <span className="text-lg font-semibold">
              Trains ({from} -{'>'} {to})
            </span>
            <img
              src={CloseIcon}
              alt="close popup"
              onClick={() => setPopupVisible(false)}
            />
          </div>
          <TrainList
            trains={trains}
            onSelectTrain={t => {
              setTrain(t);
              setPopupVisible(false);
            }}
          />
        </div>
      )}
    </>
  );
};

export default DelayPredictionCard;
