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

export type TrainDelayRequest = {
  route: string;
  train_number: string;
  depart_time: string;
};

export type TrainDelayResponse = {
  label: string;
  score: number;
};

export type RoutesResponse = {
  routes: Array<string>;
};

export type Route = {
  from: string;
  to: string;
  value: string;
};
