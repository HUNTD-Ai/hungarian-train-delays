INSERT INTO monthly_sum_delays (
  timestamp,
  delay
) SELECT 
  extract(
    epoch
    FROM (DATE_TRUNC('month', jd.journey_start))::timestamp
  )*1000 AS month,
  SUM(journey_avg_delay) AS mothly_sum_journey_delay
FROM journey_delays jd
WHERE
  jd.journey_start > DATE_TRUNC('month', CURRENT_DATE)::timestamp
GROUP BY month
ORDER BY month
ON CONFLICT (timestamp)
DO UPDATE
SET delay = EXCLUDED.delay;
