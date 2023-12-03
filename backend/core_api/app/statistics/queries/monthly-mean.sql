INSERT INTO monthly_mean_delays (
  timestamp,
  delay
) SELECT 
  extract(
    epoch
    FROM (DATE_TRUNC('month', jd.journey_start))::timestamp
  )*1000 AS month,
  AVG(journey_avg_delay) AS mothly_mean_journey_delay
FROM journey_delays jd
WHERE
  jd.journey_start > DATE_TRUNC('month', CURRENT_DATE)::timestamp
GROUP BY month
ORDER BY month
ON CONFLICT (timestamp)
DO UPDATE
SET delay = EXCLUDED.delay;
