#!/bin/bash
# This is the script that is installed on the server. Changing it will do nothing. If you need to
# make changes to the script let Yaseen know and he can do it.

IMAGE_NAME="subjecthub-backend"
REPO="yaseenk/subjecthub"
TAG="latest"

echo -e "\n\nStopping image if it exists. No need to fret if a warning comes up"
docker stop $IMAGE_NAME || true && docker rm $IMAGE_NAME || true
echo -e "\n\nPulling latest changes"
docker pull $REPO:$TAG
echo -e "\n\nRunning new version of backed on port 80"
docker run -d -p 80:8080 --name $IMAGE_NAME $REPO:$TAG
