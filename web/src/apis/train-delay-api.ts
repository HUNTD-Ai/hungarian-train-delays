import ApiConstants from './api-constants.ts';
import {
  Route,
  RoutesResponse,
  TimetableResponse,
  Train,
  PredictionRequest,
  PredictionResponse,
  LiveDataResponse,
  DelayStat,
  StatResponse,
  HighestDelayRequest,
  MeanDelaysPerRouteRequest,
  MeanDelaysPerRouteResponse,
} from '../models/models.ts';

export const TrainDelayApi = {
  getRoutes: async (): Promise<Array<Route>> => {
    try {
      const response = await fetch(
        ApiConstants.CORE_BASE_URL + '/stats/routes',
      );
      const routesResponse: RoutesResponse = await response.json();
      return routesResponse.routes.map(x => {
        const [from, to] = x.split(' - ');
        return {
          from: from,
          to: to,
          value: x,
        };
      });
    } catch (e) {
      return [];
    }
  },
  getTimetable: async (
    route: string,
    date: string,
  ): Promise<TimetableResponse | null> => {
    try {
      const response = await fetch(
        ApiConstants.CORE_BASE_URL + '/stats/timetable',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            route: route,
            depart_date: date,
          }),
        },
      );

      return await response.json();
    } catch (e) {
      return null;
    }
  },
  getLiveData: async (
    route: string,
    trainNumber: string,
  ): Promise<LiveDataResponse | null> => {
    try {
      const response = await fetch(ApiConstants.CORE_BASE_URL + '/stats/live', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          route: route,
          trainNumber: trainNumber,
        }),
      });

      if (!response.ok) {
        return null;
      }

      return await response.json();
    } catch (e) {
      return null;
    }
  },
  predictDelayProbability: async (
    train: Train,
    date: string,
  ): Promise<PredictionResponse | null> => {
    try {
      const input: PredictionRequest = {
        route: train.route,
        train_number: train.trainNumber,
        depart_time: date + 'T' + train.departureTime,
      };
      const response = await fetch(
        ApiConstants.DELAY_PREDICTION_BASE_URL + '/predict',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(input),
        },
      );
      return await response.json();
    } catch (e) {
      return null;
    }
  },
  predictDelayCause: async (
    train: Train,
    date: string,
  ): Promise<PredictionResponse | null> => {
    try {
      const input: PredictionRequest = {
        route: train.route,
        train_number: train.trainNumber,
        depart_time: date + 'T' + train.departureTime,
      };
      const response = await fetch(
        ApiConstants.DELAY_CAUSE_PREDICTION_BASE_URL + '/predict',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(input),
        },
      );
      return await response.json();
    } catch (e) {
      return null;
    }
  },
  getMonthlyMean: async (): Promise<Array<DelayStat> | null> => {
    try {
      const response = await fetch(
        ApiConstants.CORE_BASE_URL + '/stats/monthly-mean',
      );
      const statResponse: StatResponse = await response.json();
      return statResponse.delays.map(x => ({
        timestamp: new Date(x.timestamp),
        delay: x.delay,
      }));
    } catch (e) {
      return null;
    }
  },
  getMonthlySums: async (): Promise<Array<DelayStat> | null> => {
    try {
      const response = await fetch(
        ApiConstants.CORE_BASE_URL + '/stats/monthly-sum',
      );
      const statResponse: StatResponse = await response.json();
      return statResponse.delays.map(x => ({
        timestamp: new Date(x.timestamp),
        delay: x.delay,
      }));
    } catch (e) {
      return null;
    }
  },
  getHighestDelay: async (days: number): Promise<Array<DelayStat> | null> => {
    try {
      const start = new Date();
      const end = new Date();
      start.setDate(end.getDate() - days);

      const input: HighestDelayRequest = {
        startTimestamp: start,
        endTimestamp: end,
      };

      const response = await fetch(
        ApiConstants.CORE_BASE_URL + '/stats/highest-delay',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(input),
        },
      );
      const statResponse: StatResponse = await response.json();
      return statResponse.delays.map(x => ({
        timestamp: new Date(x.timestamp),
        delay: x.delay,
      }));
    } catch (e) {
      return null;
    }
  },
  getMeanDelaysPerRoute: async (
    route: string,
  ): Promise<Array<DelayStat> | null> => {
    try {
      const start = new Date();
      const end = new Date();
      start.setMonth(1);

      const input: MeanDelaysPerRouteRequest = {
        route: route,
        startTimestamp: start,
        endTimestamp: end,
      };

      const response = await fetch(
        ApiConstants.CORE_BASE_URL + '/stats/mean-route-delay',
        {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(input),
        },
      );
      const statResponse: MeanDelaysPerRouteResponse = await response.json();
      return statResponse.delays.delays.map(x => ({
        timestamp: new Date(x.timestamp),
        delay: x.delay,
      }));
    } catch (e) {
      return null;
    }
  },
};
