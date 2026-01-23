#!/bin/bash
DEPLOY_DIR="/home/ec2-user/backend/deploy"

echo "===== ApplicationStart started ====="

echo ">>> Docker Compose 실행"
cd "$DEPLOY_DIR"
docker-compose up -d

echo "===== ApplicationStart completed ====="