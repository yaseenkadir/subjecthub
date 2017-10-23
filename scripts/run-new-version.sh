#!/bin/bash
# This script is run by travis during a master branch deploy.

# It assumes that the postgresql container is running!

source /home/spring/.subject-hub/secrets.sh
IMAGE_NAME="subjecthub-backend"
REPO="yaseenk/subjecthub"
TAG="latest"

echo -e "Stopping image if it exists. No need to fret if a warning comes up."
docker stop $IMAGE_NAME || true && docker rm $IMAGE_NAME || true
echo -e "\nLogging in"
docker login -u="$DOCKER_USERNAME" -p="$DOCKER_PASSWORD";
unset DOCKER_USERNAME
unset DOCKER_PASSWORD
echo -e "\nPulling latest changes"
docker pull $REPO:$TAG
echo -e "\nRunning new version of backend on port 80, 8080 and 443"

# Spring will handle http -> https rerouting
docker run \
    -v /etc/letsencrypt/:/etc/letsencrypt/ \
    -e KEYSTORE_PASSWORD="$KEYSTORE_PASSWORD" \
    -e JWT_SECRET_KEY="$JWT_SECRET_KEY" \
    -e SUBJECT_HUB_DB_PASSWORD="$SUBJECT_HUB_DB_PASSWORD" \
    -d \
    --restart unless-stopped \
    -p 80:80 \
    -p 443:443 \
    --link subjecthubdb:postgres \
    --name $IMAGE_NAME $REPO:$TAG
unset KEYSTORE_PASSWORD
unset JWT_SECRET_KEY
unset SUBJECT_HUB_DB_PASSWORD
