import ApiConstants from './api-constants.ts';
import {
  Route,
  RoutesResponse,
  TrainDelayRequest,
  TrainDelayResponse,
} from '../models/models.ts';

export const TrainDelayApi = {
  getRoutes: async (): Promise<Array<Route>> => {
    try {
      const response: Response = await fetch(
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
  predictDelayProbability: async (
    from: string,
    to: string,
    trainNumber: string,
    date: Date,
  ): Promise<number> => {
    const input: TrainDelayRequest = {
      route: from + ' - ' + to,
      train_number: trainNumber,
      depart_time: `${
        date.toISOString().split('T')[0]
      } ${date.getHours()}:${date.getMinutes()}`,
    };
    const response = await fetch(ApiConstants.DelayProbabilityApiUrl, {
      body: JSON.stringify(input),
      method: 'POST',
    });
    const { score } = (await response.json()) as TrainDelayResponse;
    return score;
  },
  predictDelayCause: async (
    from: string,
    to: string,
    trainNumber: string,
    date: Date,
  ): Promise<string> => {
    const input: TrainDelayRequest = {
      route: from + ' - ' + to,
      train_number: trainNumber,
      depart_time: `${
        date.toISOString().split('T')[0]
      } ${date.getHours()}:${date.getMinutes()}`,
    };
    const response = await fetch(ApiConstants.DelayCauseApiUrl, {
      body: JSON.stringify(input),
      method: 'POST',
    });
    const { label } = (await response.json()) as TrainDelayResponse;
    return label;
  },
};
