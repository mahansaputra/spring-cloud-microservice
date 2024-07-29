#!/bin/sh

# Set variables
IMAGE_NAME="mahansa/gateway-ms"
TAG="latest"

# Build the Docker image
docker build -t $IMAGE_NAME:$TAG .

# Check if the build was successful
if [ $? -eq 0 ]; then
  echo
  echo "Docker image built successfully."
  echo
else
  echo
  echo "Docker build failed."
  echo
  exit 1
fi

# Push the Docker image to the repository
docker push $IMAGE_NAME:$TAG

# Check if the push was successful
if [ $? -eq 0 ]; then
  echo
  echo "Docker image pushed successfully."
  echo
else
  echo
  echo "Docker push failed."
  echo
  exit 1
fi
