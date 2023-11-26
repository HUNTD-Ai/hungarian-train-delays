import { useEffect, useState } from 'react';
import { DelayStat } from '../models/models.ts';
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

  useEffect(() => {
    const fetchMonthlyMeans = async () => {
      const result1 = await TrainDelayApi.getMonthlyMean();
      setMonthlyMeans(result1);
      console.log('fetchMonthlyMeans', result1);
    };

    const fetchMonthlySums = async () => {
      const result1 = await TrainDelayApi.getMonthlySums();
      setMonthlySum(result1);
      console.log('setMonthlySum', result1);
    };

    fetchMonthlyMeans();
    fetchMonthlySums();
  }, []);

  return (
    <div className="flex h-full w-full flex-col items-center gap-y-4 overflow-y-auto bg-backgroundLight px-5 py-8 text-textColor dark:bg-backgroundDark">
      {monthlyMeans != null && (
        <div className="flex w-full flex-col rounded-[25px] bg-cardBackgroundLight drop-shadow-md dark:bg-cardBackgroundDark sm:w-[512px]">
          <div className="flex h-12 w-full items-center rounded-t-[25px] bg-primaryColor px-6 sm:px-12">
            <span className="text-lg font-semibold">
              Monthly mean delays ({monthlyMeans[0].timestamp.getFullYear()})
            </span>
          </div>
          <div className="flex flex-col items-center gap-y-[9px] px-4 py-3 sm:gap-y-[18px] sm:px-8 sm:py-6">
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
        <div className="flex w-full flex-col rounded-[25px] bg-cardBackgroundLight drop-shadow-md dark:bg-cardBackgroundDark sm:w-[512px]">
          <div className="flex h-12 w-full items-center rounded-t-[25px] bg-primaryColor px-6 sm:px-12">
            <span className="text-lg font-semibold">
              Total delay per month ({monthlySum[0].timestamp.getFullYear()})
            </span>
          </div>
          <div className="flex flex-col items-center gap-y-[9px] px-4 py-3 sm:gap-y-[18px] sm:px-8 sm:py-6">
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

      {/*<div className="flex w-full flex-col rounded-[25px] bg-cardBackgroundLight drop-shadow-md dark:bg-cardBackgroundDark sm:w-[512px]">*/}
      {/*  <div className="flex h-12 w-full items-center rounded-t-[25px] bg-primaryColor px-6 sm:px-12">*/}
      {/*    <span className="text-lg font-semibold">Train info</span>*/}
      {/*  </div>*/}
      {/*  <div className="flex flex-col items-center gap-y-[9px] px-4 py-3 sm:gap-y-[18px] sm:px-8 sm:py-6">*/}
      {/*    <div className="h-20 w-20 flex-shrink-0 bg-orange-400"></div>*/}
      {/*  </div>*/}
      {/*</div>*/}

      {/*<div className="flex w-full flex-col rounded-[25px] bg-cardBackgroundLight drop-shadow-md dark:bg-cardBackgroundDark sm:w-[512px]">*/}
      {/*  <div className="flex h-12 w-full items-center rounded-t-[25px] bg-primaryColor px-6 sm:px-12">*/}
      {/*    <span className="text-lg font-semibold">Train info</span>*/}
      {/*  </div>*/}
      {/*  <div className="flex flex-col items-center gap-y-[9px] px-4 py-3 sm:gap-y-[18px] sm:px-8 sm:py-6">*/}
      {/*    <div className="h-20 w-20 flex-shrink-0 bg-orange-400"></div>*/}
      {/*  </div>*/}
      {/*</div>*/}

      {/*<div className="flex w-full flex-col rounded-[25px] bg-cardBackgroundLight drop-shadow-md dark:bg-cardBackgroundDark sm:w-[512px]">*/}
      {/*  <div className="flex h-12 w-full items-center rounded-t-[25px] bg-primaryColor px-6 sm:px-12">*/}
      {/*    <span className="text-lg font-semibold">Train info</span>*/}
      {/*  </div>*/}
      {/*  <div className="flex flex-col items-center gap-y-[9px] px-4 py-3 sm:gap-y-[18px] sm:px-8 sm:py-6">*/}
      {/*    <div className="h-20 w-20 flex-shrink-0 bg-orange-400"></div>*/}
      {/*  </div>*/}
      {/*</div>*/}
    </div>
  );
};

export default StatisticsPage;
