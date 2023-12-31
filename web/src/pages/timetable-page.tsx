import { useLocation, useNavigate } from 'react-router-dom';
import { useCallback, useEffect, useState } from 'react';
import { Route, Train } from '../models/models.ts';
import { TrainDelayApi } from '../apis/train-delay-api.ts';
import { TrainIcon } from '../components/icons.tsx';
import LoadingSpinner from '../components/loading-spinner.tsx';

const TimetablePage = () => {
  const location = useLocation();
  const navigate = useNavigate();

  const [route, setRoute] = useState<string>('');
  const [trainsLoading, setTrainsLoading] = useState<boolean>(false);
  const [trains, setTrains] = useState<Array<Train>>([]);

  const [travelDate, setTravelDate] = useState<string>('');
  const [selectedTrain, setSelectedTrain] = useState<Train | null>(null);

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
          travelDistance: t.length_km,
        });
      }
    });
    setTrains(timetableTrains);
    setTrainsLoading(false);

    console.log('trains', timetableTrains);
  }, []);

  useEffect(() => {
    const params = new URLSearchParams(location.search);

    const from = params.get('from');
    const to = params.get('to');
    if (from == null || to == null) {
      navigate('/');
      return;
    }

    let date = params.get('date');
    if (date == null /* or not in format yyyy-mm-dd*/) {
      date = new Date().toISOString().split('T')[0];
    }
    setTravelDate(new Date(`${date}T00:00:00`).toLocaleDateString());

    const route = {
      from: from,
      to: to,
      value: `${from} - ${to}`,
    } as Route;
    setRoute(route.value);
    fetchTimeTable(route, date);
  }, [navigate, location, fetchTimeTable]);

  const navigateBack = () => {
    navigate(-1);
  };

  const predict = useCallback(() => {
    // TODO after choosing a concrete train, send a prediction request
    console.log('predict');
  }, []);

  const mapToTravelTimeString = (timeString: string) => {
    const [hours, minutes] = timeString.split(':');
    if (Number.parseInt(hours) === 0) {
      return `${minutes}m`;
    } else {
      return `${Number.parseInt(hours)}h ${minutes}m`;
    }
  };

  return (
    <div
      id="window-container"
      className="flex h-[calc(100dvh-64px)] w-full items-start justify-center bg-backgroundLight p-4 dark:bg-backgroundDark">
      <div
        id="timeable-window"
        className="flex h-full w-full flex-shrink-0 flex-grow-0 flex-col items-start sm:w-[512px] md:w-[640px] lg:w-[768px]">
        <div
          id="timetable-header"
          className="flex h-12 w-full flex-shrink-0 flex-row items-center rounded-t-lg bg-primaryColor px-3 font-bold text-textColor">
          {route} ({travelDate})
        </div>
        <div
          id="timetable-content"
          className="flex h-[calc(100%-3rem)] w-full flex-col items-start rounded-b-lg bg-cardBackgroundLight dark:bg-cardBackgroundDark">
          {trainsLoading && (
            <div className="flex h-full w-full items-center justify-center">
              <LoadingSpinner />
            </div>
          )}

          {!trainsLoading && (
            <>
              {trains.length === 0 && (
                <div className="flex h-full w-full items-center justify-center">
                  <span className="text:text-textBoxTextColorLight text-xl font-bold dark:text-textBoxTextColorDark">
                    No trains found
                  </span>
                </div>
              )}

              {trains.length > 0 && (
                <div
                  id="timetable-table-container"
                  className="h-full w-full overflow-y-scroll p-2 sm:flex sm:flex-grow-0 sm:items-start sm:justify-center sm:p-4">
                  <table className="text:text-textBoxTextColorLight max-h-full w-full rounded-lg bg-textBoxBackgroundLight dark:bg-textBoxBackgroundDark dark:text-textBoxTextColorDark">
                    <thead>
                      <tr className="whitespace-nowrap border-b-2 border-[#e5e7eb]">
                        <th className="p-2 text-start">Train number</th>
                        <th className="p-2 text-start">Departure time</th>
                        <th className="p-2 text-start">Arrival time</th>
                        <th className="p-2 text-start">Travel time</th>
                        <th className="p-2 text-start">Distance</th>
                      </tr>
                    </thead>
                    <tbody className="rounded-lg">
                      {trains
                        // .filter((_, i) => i < 3)
                        // .filter(() => false)
                        .map((train, index, items) => (
                          <tr
                            key={`${train.departureTime}-${train.arrivalTime}`}
                            className={
                              index === items.length - 1
                                ? 'hover:bg-tableSelectedRowBackgroundLight hover:dark:bg-tableSelectedRowBackgroundDark h-16 whitespace-nowrap rounded-b-lg sm:h-12'
                                : 'hover:bg-tableSelectedRowBackgroundLight hover:dark:bg-tableSelectedRowBackgroundDark h-16 whitespace-nowrap border-b border-[#e5e7eb] sm:h-12'
                            }
                            onClick={() => setSelectedTrain(train)}>
                            <td
                              className={
                                train === selectedTrain
                                  ? index === items.length - 1
                                    ? 'bg-tableSelectedRowBackgroundLight dark:bg-tableSelectedRowBackgroundDark flex h-16 items-center gap-x-2 rounded-bl-lg p-2 sm:h-12'
                                    : 'bg-tableSelectedRowBackgroundLight dark:bg-tableSelectedRowBackgroundDark flex h-16 items-center gap-x-2 p-2 sm:h-12'
                                  : index === items.length - 1
                                  ? 'rounded-bg-lg flex h-16 items-center gap-x-2 p-2 sm:h-12'
                                  : 'flex h-16 items-center gap-x-2 p-2 sm:h-12'
                              }>
                              <TrainIcon />
                              <span>{train.trainNumber}</span>
                            </td>
                            <td
                              className={
                                train === selectedTrain
                                  ? 'bg-tableSelectedRowBackgroundLight dark:bg-tableSelectedRowBackgroundDark p-2 text-end'
                                  : 'p-2 text-end'
                              }>
                              {train.departureTime}
                            </td>
                            <td
                              className={
                                train === selectedTrain
                                  ? 'bg-tableSelectedRowBackgroundLight dark:bg-tableSelectedRowBackgroundDark p-2 text-end'
                                  : 'p-2 text-end'
                              }>
                              {train.arrivalTime}
                            </td>
                            <td
                              className={
                                train === selectedTrain
                                  ? 'bg-tableSelectedRowBackgroundLight dark:bg-tableSelectedRowBackgroundDark p-2 text-end'
                                  : 'p-2 text-end'
                              }>
                              {mapToTravelTimeString(train.travelTime)}
                            </td>
                            <td
                              className={
                                train === selectedTrain
                                  ? index === items.length - 1
                                    ? 'bg-tableSelectedRowBackgroundLight dark:bg-tableSelectedRowBackgroundDark rounded-br-lg p-2 text-end'
                                    : 'bg-tableSelectedRowBackgroundLight dark:bg-tableSelectedRowBackgroundDark p-2 text-end'
                                  : index === items.length - 1
                                  ? 'rounded-br-lg p-2 text-end'
                                  : 'p-2 text-end'
                              }>
                              {train.travelDistance}
                            </td>
                          </tr>
                        ))}
                    </tbody>
                  </table>
                </div>
              )}

              <div
                id="timetable-navigation-buttons"
                className="flex w-full flex-row items-center gap-x-2 px-2 py-1 sm:px-4 sm:py-2">
                <button
                  className="h-12 w-full rounded-lg bg-primaryColor font-bold text-textColor hover:bg-primaryColorHover"
                  onClick={() => navigateBack()}>
                  Back
                </button>

                {trains.length > 0 && (
                  <button
                    className="h-12 w-full rounded-lg bg-primaryColor font-bold text-textColor hover:bg-primaryColorHover disabled:cursor-not-allowed disabled:bg-opacity-[42%] disabled:text-gray-300 hover:disabled:bg-primaryColor hover:disabled:bg-opacity-[42%]"
                    type="submit"
                    disabled={selectedTrain == null}
                    onClick={() => predict()}>
                    I'm feeling lucky
                  </button>
                )}
              </div>
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default TimetablePage;
