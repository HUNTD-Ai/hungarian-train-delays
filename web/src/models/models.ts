export type Train = {
  serviceName: string;
  trainNumber: string;
  departureTime: Date;
  arrivalTime: Date;
  travelTime: Date;
};

export type PredictionResult = {
  label: string;
  score: number;
  delay: number;
};
