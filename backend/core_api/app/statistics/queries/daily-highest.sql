INSERT INTO daily_record (
  timestamp,
  delay
) SELECT 
  extract(
    epoch
    FROM (DATE_TRUNC('day', jd.journey_start))::timestamp
  )*1000 AS day,
  MAX(journey_avg_delay) AS highest_delay
FROM 
  journey_delays jd
WHERE
  jd.journey_start > DATE_TRUNC('day', CURRENT_DATE)::timestamp
GROUP BY day
ORDER BY day
ON CONFLICT (timestamp)
DO UPDATE
SET delay = EXCLUDED.delay;
