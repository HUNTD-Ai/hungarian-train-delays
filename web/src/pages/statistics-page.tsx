import React, { useEffect, useState } from 'react';
import { DelayStat, Route } from '../models/models.ts';
import { TrainDelayApi } from '../apis/train-delay-api.ts';
import { Chart as ChartJS, registerables } from 'chart.js';
import { Bar } from 'react-chartjs-2';
import { Month } from '../models/month.ts';

ChartJS.register(...registerables);

const chartOptions = {
  responsive: true,
  plugins: {
    legend: {
      display: false,
    },
  },
  scales: {
    y: {
      beginAtZero: true,
    },
  },
};

const StatisticsPage = () => {
  const [monthlyMeans, setMonthlyMeans] = useState<Array<DelayStat> | null>(
    null,
  );
  const [monthlySum, setMonthlySum] = useState<Array<DelayStat> | null>(null);
  const [highestDelays7Days, setHighestDelays7Days] =
    useState<Array<DelayStat> | null>(null);
  const [highestDelays30Days, setHighestDelays30Days] =
    useState<Array<DelayStat> | null>(null);

  const [routes, setRoutes] = useState<Array<Route> | null>();
  const [departureStations, setDepartureStations] = useState<Array<string>>([]);
  const [arrivalStations, setArrivalStations] = useState<Array<string>>([]);
  const [from, setFrom] = useState<string | undefined>(undefined);
  const [to, setTo] = useState<string | undefined>(undefined);
  const [route, setRoute] = useState<Route | null>(null);
  const [delaysPerRoute, setDelaysPerRoute] = useState<Array<DelayStat> | null>(
    null,
  );

  useEffect(() => {
    const fetchMonthlyMeans = async () => {
      const result = await TrainDelayApi.getMonthlyMean();
      setMonthlyMeans(result);
    };

    const fetchMonthlySums = async () => {
      const result = await TrainDelayApi.getMonthlySums();
      setMonthlySum(result);
    };

    const fetchHighestDelays7Days = async () => {
      const result = await TrainDelayApi.getHighestDelay(7);
      setHighestDelays7Days(result);
    };

    const fetchHighestDelays30Days = async () => {
      const result = await TrainDelayApi.getHighestDelay(30);
      setHighestDelays30Days(result);
    };

    fetchMonthlyMeans();
    fetchMonthlySums();
    fetchHighestDelays7Days();
    fetchHighestDelays30Days();
  }, []);

  const onSelectDepartureStation = (event: {
    target: { value: React.SetStateAction<string | undefined> };
  }) => setFrom(event.target.value);

  const onSelectArrivalStation = (event: {
    target: { value: React.SetStateAction<string | undefined> };
  }) => setTo(event.target.value);

  useEffect(() => {
    const fetchRoutes = async () => {
      const routes = await TrainDelayApi.getRoutes();
      setRoutes(routes);
    };
    fetchRoutes();
  }, []);

  useEffect(() => {
    if (routes != null) {
      const stations = Array.from(new Set(routes.map(r => r.from)));
      stations.sort();
      setDepartureStations(stations);
    }
  }, [routes]);

  useEffect(() => {
    if (routes != null && from != undefined) {
      const stations = routes
        .filter(r => r.from === from)
        .map(r => r.to)
        .sort();
      setArrivalStations(stations);
    }
  }, [routes, from]);

  useEffect(() => {
    const selectedRoute =
      routes?.find(x => x.from === from && x.to === to) ?? null;
    setRoute(selectedRoute);
  }, [routes, from, to]);

  useEffect(() => {
    if (route != null) {
      const fetchMeanDelayPerRoute = async () => {
        const result = await TrainDelayApi.getMeanDelaysPerRoute(route.value);
        setDelaysPerRoute(result);
      };
      fetchMeanDelayPerRoute();
    }
  }, [route]);

  return (
    <div className="flex h-full w-full flex-col items-center gap-y-4 overflow-y-auto bg-backgroundLight px-5 py-8 text-textColor dark:bg-backgroundDark">
      {monthlyMeans != null && (
        <div className="flex w-full flex-col rounded-[25px] bg-cardBackgroundLight drop-shadow-md dark:bg-cardBackgroundDark sm:w-[512px] md:w-[640px] lg:w-[768px]">
          <div className="flex h-12 w-full items-center rounded-t-[25px] bg-primaryColor px-6 sm:px-12">
            <span className="text-lg font-semibold">
              Monthly mean delays ({monthlyMeans[0].timestamp.getFullYear()})
            </span>
          </div>
          <div className="flex flex-col items-center justify-center gap-y-[9px] px-4 py-3 sm:h-72 sm:gap-y-[18px] sm:px-8 sm:py-6 md:h-96 lg:h-[448px]">
            <Bar
              data={{
                labels: monthlyMeans.map(x => Month[x.timestamp.getMonth()]),
                datasets: [
                  {
                    label: 'Monthly mean delays',
                    data: monthlyMeans.map(x => x.delay),
                    borderColor: '#4D4BE2',
                    backgroundColor: '#6665DD',
                    hoverBackgroundColor: '#4D4BE2',
                    borderWidth: 1,
                  },
                ],
              }}
              options={chartOptions}
            />
          </div>
        </div>
      )}

      {monthlySum != null && (
        <div className="flex w-full flex-col rounded-[25px] bg-cardBackgroundLight drop-shadow-md dark:bg-cardBackgroundDark sm:w-[512px] md:w-[640px] lg:w-[768px]">
          <div className="flex h-12 w-full items-center rounded-t-[25px] bg-primaryColor px-6 sm:px-12">
            <span className="text-lg font-semibold">
              Total delay per month ({monthlySum[0].timestamp.getFullYear()})
            </span>
          </div>
          <div className="flex flex-col items-center justify-center gap-y-[9px] px-4 py-3 sm:h-72 sm:gap-y-[18px] sm:px-8 sm:py-6 md:h-96 lg:h-[448px]">
            <Bar
              data={{
                labels: monthlySum.map(x => Month[x.timestamp.getMonth()]),
                datasets: [
                  {
                    label: 'Total delay per month',
                    data: monthlySum.map(x => x.delay),
                    borderColor: '#4D4BE2',
                    backgroundColor: '#6665DD',
                    hoverBackgroundColor: '#4D4BE2',
                    borderWidth: 1,
                  },
                ],
              }}
              options={chartOptions}
            />
          </div>
        </div>
      )}

      {highestDelays7Days != null && (
        <div className="flex w-full flex-col rounded-[25px] bg-cardBackgroundLight drop-shadow-md dark:bg-cardBackgroundDark sm:w-[512px] md:w-[640px] lg:w-[768px]">
          <div className="flex h-12 w-full items-center rounded-t-[25px] bg-primaryColor px-6 sm:px-12">
            <span className="text-lg font-semibold">
              Highest delays (last 7 days)
            </span>
          </div>
          <div className="flex flex-col items-center justify-center gap-y-[9px] px-4 py-3 sm:h-72 sm:gap-y-[18px] sm:px-8 sm:py-6 md:h-96 lg:h-[448px]">
            <Bar
              data={{
                labels: highestDelays7Days.map(x => x.timestamp.toDateString()),
                datasets: [
                  {
                    label: 'Total delay per month',
                    data: highestDelays7Days.map(x => x.delay),
                    borderColor: '#4D4BE2',
                    backgroundColor: '#6665DD',
                    hoverBackgroundColor: '#4D4BE2',
                    borderWidth: 1,
                  },
                ],
              }}
              options={chartOptions}
            />
          </div>
        </div>
      )}

      {highestDelays30Days != null && (
        <div className="flex w-full flex-col rounded-[25px] bg-cardBackgroundLight drop-shadow-md dark:bg-cardBackgroundDark sm:w-[512px] md:w-[640px] lg:w-[768px]">
          <div className="flex h-12 w-full items-center rounded-t-[25px] bg-primaryColor px-6 sm:px-12">
            <span className="text-lg font-semibold">
              Highest delays (past 30 days)
            </span>
          </div>
          <div className="flex flex-col items-center justify-center gap-y-[9px] px-4 py-3 sm:h-72 sm:gap-y-[18px] sm:px-8 sm:py-6 md:h-96 lg:h-[448px]">
            <Bar
              data={{
                labels: highestDelays30Days.map(x =>
                  x.timestamp.toDateString(),
                ),
                datasets: [
                  {
                    label: 'Total delay per month',
                    data: highestDelays30Days.map(x => x.delay),
                    borderColor: '#4D4BE2',
                    backgroundColor: '#6665DD',
                    hoverBackgroundColor: '#4D4BE2',
                    borderWidth: 1,
                  },
                ],
              }}
              options={chartOptions}
            />
          </div>
        </div>
      )}

      <div className="flex w-full flex-col rounded-[25px] bg-cardBackgroundLight drop-shadow-md dark:bg-cardBackgroundDark sm:w-[512px] md:w-[640px] lg:w-[768px]">
        <div className="flex h-12 w-full items-center rounded-t-[25px] bg-primaryColor px-6 sm:px-12">
          <span className="text-lg font-semibold">
            Mean delays per route {route != null ? '(' + route.value + ')' : ''}
          </span>
        </div>
        <div className="flex flex-col items-center gap-y-[9px] px-4 py-3 sm:h-72 sm:gap-y-[18px] sm:px-8 sm:py-6 md:h-96 lg:h-[448px]">
          <div className="flex w-full flex-col gap-2 sm:flex-row">
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
          </div>
          {delaysPerRoute != null && (
            <Bar
              data={{
                labels: delaysPerRoute.map(x => x.timestamp.toDateString()),
                datasets: [
                  {
                    data: delaysPerRoute.map(x => x.delay),
                    borderColor: '#4D4BE2',
                    backgroundColor: '#6665DD',
                    hoverBackgroundColor: '#4D4BE2',
                    borderWidth: 1,
                  },
                ],
              }}
              options={chartOptions}
            />
          )}
        </div>
      </div>
    </div>
  );
};

export default StatisticsPage;
