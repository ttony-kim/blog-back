#!/bin/bash
echo "===== Validate Service started ====="

echo ">>> Health Check"

URL="http://localhost/api/health-check"
MAX_RETRIES=10  # 최대 헬스체크 재시도 횟수
SLEEP_SEC=2     # 재시도 간격(초)

retry_count=0

while true; do
  status_code=$(curl -s -o /dev/null -w "%{http_code}" $URL)

  if [ "$status_code" -eq 200 ]; then
    echo "Service is healthy."
    echo "===== Validate Service completed ====="
    exit 0
  else
    echo "Waiting for service. status code: $status_code"
    sleep $SLEEP_SEC
  fi

  retry_count=$((retry_count+1))

  if [ $retry_count -ge $MAX_RETRIES ]; then
    echo "ERROR: Health check failed."
    echo "Final status code: $status_code"
    exit 1
  fi
done

