import DelayPredictionCard from '../components/delay-prediction-card.tsx';

const HomePage = () => {
  return (
    <div className="flex h-full w-full items-center justify-center bg-backgroundLight text-textColor dark:bg-backgroundDark">
      <DelayPredictionCard />
    </div>
  );
};

export default HomePage;
