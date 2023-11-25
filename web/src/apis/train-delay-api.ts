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
};
