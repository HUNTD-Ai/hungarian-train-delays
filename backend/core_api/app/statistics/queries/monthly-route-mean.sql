INSERT INTO monthly_mean_route_delays(
  timestamp,
  route,
  delay
) SELECT
  extract(
    epoch
    FROM (DATE_TRUNC('month', jd.journey_start))::timestamp
  )*1000 AS "month",
  td.relation AS route,
  COALESCE(AVG(journey_avg_delay),0) AS mothly_avg_journey_delay
FROM journey_delays jd
INNER JOIN train_data td
ON td.elvira_id = jd.elvira_id
WHERE
  jd.journey_start > DATE_TRUNC('month', CURRENT_DATE)::timestamp
GROUP BY "month", td.relation
ORDER BY "month", td.relation
ON CONFLICT (timestamp, route)
DO UPDATE
SET delay = EXCLUDED.delay;
