## Finance Tracker

### Structure 
Each maven module in the root project is a single microservice

### Requirements
To run the example applications you need to have installed:
1. JDK 11
1. Maven (Optional)
1. Docker

### Usage 
1. To build all microservices run the command `./mvnw clean install`
1. To run all services run the command `docker-compose up -d`

### Deployment
For local deployment install: 
[Kubernetes Dashboard](https://www.replex.io/blog/how-to-install-access-and-add-heapster-metrics-to-the-kubernetes-dashboard)

   
1. kubectl apply -f ingress.yaml
1. kubectl apply -f postgre-config.yaml
1. kubectl apply -f postgres-deployment.yaml
1. kubectl apply -f keycloak-config.yaml
1. kubectl create configmap keycloak-realm-config --from-file .\realm-config\
1. kubectl apply -f keycloak-deployment.yaml
1. kubectl apply -f gateway-deployment.yaml
1. kubectl apply -f uaa-service-deployment.yaml

### Starting the project
bootstrap.yml is need to work with k8s configmaps and secrets



