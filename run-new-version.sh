#!/bin/bash
# This is the script that is installed on the server. Changing it will do nothing. If you need to
# make changes to the script let Yaseen know and he can do it.

DOCKER_USERNAME=$1
DOCKER_PASSWORD=$2
IMAGE_NAME="subjecthub-backend"
REPO="yaseenk/subjecthub"
TAG="latest"

echo -e "Stopping image if it exists. No need to fret if a warning comes up."
docker stop $IMAGE_NAME || true && docker rm $IMAGE_NAME || true
echo -e "\n\nLogging in"
docker login -u="$DOCKER_USERNAME" -p="$DOCKER_PASSWORD";
echo -e "\n\nPulling latest changes"
docker pull $REPO:$TAG
echo -e "\n\nRunning new version of backed on port 80"
docker run -d -p 443:8443 --name $IMAGE_NAME $REPO:$TAG
