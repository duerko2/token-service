#!/bin/bash
set -e
mvn clean package
docker-compose build token-service
docker-compose up -d token-service