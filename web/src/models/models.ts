export type Train = {
  route: string;
  trainNumber: string;
  departureTime: string;
  arrivalTime: string;
  travelTime: string;
  travelDistance: string;
};

export type PredictionRequest = {
  route: string;
  train_number: string;
  depart_time: string;
};

export type PredictionResponse = {
  label: number;
  score: number;
};

export type Prediction = {
  label: number | string;
  confidenceLevel: number;
};

export type PredictionResult = {
  train: Train;
  delay: Prediction;
  delayCause: Prediction | null;
  liveData: LiveDataResponse | null;
};

export type RoutesResponse = {
  routes: Array<string>;
};

export type Route = {
  from: string;
  to: string;
  value: string;
};

export type TimetableResponse = {
  plans: Array<TimetablePlan>;
};

export type LiveDataResponse = {
  delay: number | null;
  delayCause: string | null;
};

export type TimetablePlan = {
  route: string;
  arrival_time: string;
  departure_time: string;
  changes: number;
  duration: string;
  length_km: string;
  highest_class: string;
  details: Array<PlanDetail>;
};

export type PlanDetail = {
  from_station: string;
  to_station: string;
  dep_planned_time: string;
  dep_actual_time: string;
  dep_info: string;
  arr_planned_time: string;
  arr_actual_time: string;
  arr_info: string;
  train_number: string;
};

export type StatResponse = {
  delays: Array<DelayStat>;
};

export type DelayStat = {
  timestamp: Date;
  delay: number;
};

export type HighestDelayRequest = {
  startTimestamp: Date;
  endTimestamp: Date;
};

export type MeanDelaysPerRouteRequest = {
  route: string;
  startTimestamp: Date;
  endTimestamp: Date;
};

export type MeanDelaysPerRouteResponse = {
  route: string;
  delays: {
    delays: Array<DelayStat>;
  };
};
