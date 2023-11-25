import DelayPredictionCard from '../components/delay-prediction-card.tsx';
import { useEffect, useState } from 'react';
import {
  PredictionResponse,
  PredictionResult,
  Route,
  Train,
} from '../models/models.ts';
import PredictionResultCard from '../components/prediction-result-card.tsx';
import { TrainDelayApi } from '../apis/train-delay-api.ts';
import LoadingSpinner from '../components/loading-spinner.tsx';
import TrainDataCard from '../components/train-data-card.tsx';

const HomePage = () => {
  const [routes, setRoutes] = useState<Array<Route>>([]);
  const [result, setResult] = useState<PredictionResult | null>(null);
  const [loading, setLoading] = useState<boolean>(false);

  useEffect(() => {
    const fetchRoutes = async () => {
      const routes = await TrainDelayApi.getRoutes();
      setRoutes(routes);
    };

    fetchRoutes();
  }, []);

  const onSubmit = (train: Train, date: string) => {
    const sendRequests = async () => {
      setLoading(true);

      try {
        const [delayResponse, liveDataResponse] = await Promise.all([
          TrainDelayApi.predictDelayProbability(train, date),
          TrainDelayApi.getLiveData(train.route, train.trainNumber),
        ]);
        if (delayResponse == null) {
          throw 'Something happened during the prediction. Try again later.';
        }

        // Experiencing delays
        let delayCauseResponse: PredictionResponse | null = null;
        if (delayResponse.label === 1) {
          delayCauseResponse = await TrainDelayApi.predictDelayCause(
            train,
            date,
          );
        }

        setResult({
          train: train,
          delay: {
            label: delayResponse.label,
            confidenceLevel: delayResponse.score,
          },
          delayCause:
            delayCauseResponse != null
              ? {
                  label: delayCauseResponse.label,
                  confidenceLevel: delayCauseResponse.score,
                }
              : null,
          liveData: liveDataResponse,
        });
      } catch (e) {
        alert(e);
      }
      setLoading(false);
    };

    sendRequests();
  };

  if (loading) {
    return (
      <div className="flex h-full w-full items-center justify-center">
        <LoadingSpinner />
      </div>
    );
  }

  return (
    <div className="flex h-full w-full flex-col items-center gap-y-4 overflow-y-auto bg-backgroundLight px-5 py-8 text-textColor dark:bg-backgroundDark">
      {result == null && (
        <DelayPredictionCard
          routes={routes}
          onSubmit={onSubmit}
        />
      )}
      {result != null && (
        <>
          <PredictionResultCard result={result} />
          <TrainDataCard
            train={result.train}
            liveData={result.liveData}
          />
        </>
      )}
    </div>
  );
};

export default HomePage;
