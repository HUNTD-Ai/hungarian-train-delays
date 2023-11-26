FROM node:18-alpine as build

WORKDIR /app

COPY ./package.json .
RUN npm install

COPY . .
RUN npm run build

FROM nginx:1.25-alpine-slim

COPY ./nginx.conf /etc/nginx/nginx.conf
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

LABEL org.opencontainers.image.source=https://github.com/HUNTD-Ai/hungarian-train-delays
LABEL org.opencontainers.image.description="HUNTD-Ai web"
LABEL org.opencontainers.image.licenses=MIT

