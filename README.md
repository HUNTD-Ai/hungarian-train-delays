# Hungarian train delays

**WIP**

## Backend

**OpenAPI/swagger docs can be accessed on the running servers on /docs**

To run the backend locally execute the following command from the backend folder:

```bash
docker-compose -f docker-compose.yaml --env-file <your .env file location> up -d
```
## Frontend

If you wish to self-host the frontend, you can do it by running the web app via docker: 
```bash
cd hungarian-train-delays/web
docker-compose -f docker-compose.yaml up -d
```
The web app can be accessed on port `80`, but you can modify it in the frontend's `docker-compose.yaml` file.

> The self-hosted frontend has some limitations. It uses our infrastructure by default. If you wish to change this, you should edit the constants in `hungarian-train-delays/web/src/apis/api-constants.ts`. Then you should rebuild the container.

```typescript
export default class ApiConstants {
  static readonly CORE_BASE_URL = 'https://core.example.com';
  static readonly DELAY_PREDICTION_BASE_URL = 'https://delay-pred.example.com';
  static readonly DELAY_CAUSE_PREDICTION_BASE_URL = 'https://cause-pred.example.com';
}
```
