#!/bin/bash
DEPLOY_DIR="/home/ec2-user/backend/deploy"

echo "===== ApplicationStop started ====="

if [ -d "$DEPLOY_DIR" ]; then
  echo ">>> Docker Compose 중지"
  cd "$DEPLOY_DIR"
  docker-compose down || true

  echo ">>> 기존 파일 삭제"
  cd ~
  rm -rf "${DEPLOY_DIR:?}"/*
else
  echo ">>> 디렉토리 생성"
  mkdir -p "$DEPLOY_DIR"
fi

echo "===== ApplicationStop completed ====="
