import DelayPredictionCard from '../components/delay-prediction-card.tsx';
import { useState } from 'react';
import { PredictionResult } from '../models/models.ts';
import PredictionResultCard from '../components/prediction-result-card.tsx';
import { TrainDelayApi } from '../apis/train-delay-api.ts';
import LoadingSpinner from '../components/loading-spinner.tsx';

const HomePage = () => {
  const [result, setResult] = useState<PredictionResult | null>(null);
  const [loading, setLoading] = useState<boolean>(false);

  const onSubmit = (
    from: string,
    to: string,
    trainNumber: string,
    date: Date,
  ) => {
    const sendRequests = async () => {
      setLoading(true);

      try {
        const [probability, delayCause] = await Promise.all([
          TrainDelayApi.predictDelayProbability(
            from,
            to,
            trainNumber,
            new Date(date),
          ),
          TrainDelayApi.predictDelayCause(
            from,
            to,
            trainNumber,
            new Date(date),
          ),
        ]);
        setResult({
          label: delayCause,
          score: probability,
          delay: 6.4 * 60 * 1000,
        });
      } catch (e) {
        alert(e);
        setLoading(false);
        return;
      }

      await new Promise(resolve => setTimeout(resolve, 3000));
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
    <div className="flex h-full w-full items-center justify-center bg-backgroundLight px-5 text-textColor dark:bg-backgroundDark">
      {result == null && <DelayPredictionCard onSubmit={onSubmit} />}
      {result != null && <PredictionResultCard result={result} />}
    </div>
  );
};

export default HomePage;
