#!/bin/bash
# This is the script that is installed on the server. Changing it will do nothing. If you need to
# make changes to the script let Yaseen know and he can do it.

source ~/.subject-hub/secrets.sh
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
echo -e "\nRunning new version of backend on port 80 and 443"
docker run \
    -v /etc/letsencrypt/:/etc/letsencrypt/ \
    -e KEYSTORE_PASSWORD="$KEYSTORE_PASSWORD" \
    -d \
    -p 80:8443 \
    -p 443:8443 \
    --name $IMAGE_NAME $REPO:$TAG
unset KEYSTORE_PASSWORD
