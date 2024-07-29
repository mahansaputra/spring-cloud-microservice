#Spring Cloud Microservice

Create microservice using spring cloud that connects to postgresql database, also used eureka as service discovery, zipkin as tracing system to check latency between services

First the application runs in a local docker container using docker-compose.yml to setup the environment and pull the image from docker hub

Second the application runs in local kubernetes (minikube) by using deployment, service, configmap, and statefulset yml files
