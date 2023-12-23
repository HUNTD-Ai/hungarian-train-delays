CREATE TABLE IF NOT EXISTS monthly_sum_delays(
  timestamp BIGINT PRIMARY KEY,
  delay FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS monthly_mean_delays(
  timestamp BIGINT PRIMARY KEY,
  delay FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS daily_record(
  timestamp BIGINT PRIMARY KEY,
  delay FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS monthly_mean_route_delays(
  timestamp BIGINT,
  route TEXT,
  delay FLOAT NOT NULL,
  PRIMARY KEY(timestamp, route)
);

CREATE MATERIALIZED VIEW IF NOT EXISTS journey_delays AS
SELECT
  td.elvira_id,
  to_timestamp(MIN("timestamp")/1000) AS journey_start,
  COALESCE(AVG(NULLIF(delay, 'NaN')::float), 0) AS journey_avg_delay
FROM
  train_data td
WHERE
  "timestamp" > EXTRACT(EPOCH FROM (CURRENT_DATE - INTERVAL '1 month')::timestamp) * 1000
GROUP BY elvira_id;
