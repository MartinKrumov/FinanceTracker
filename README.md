# Finance Tracker

## Structure 
Each maven module in the root project is a single microservice.

## Requirements
To run the example applications you need to have installed:
1. JDK 17
2. Maven (Optional)
3. Docker

## Usage 
1. To build all microservices run the command `./mvnw clean install`
1. To run all services run the command `docker-compose up -d`

## Deployment
For local deployment, install dashboard and ingress:

**Installing Kubernetes:**
1. [On WSL2 - KinD or Minikube](https://kubernetes.io/blog/2020/05/21/wsl-docker-kubernetes-on-the-windows-desktop/)
1. [Dashboard Official Repository](https://github.com/kubernetes/dashboard),
   [Dashboard Setup](https://upcloud.com/community/tutorials/deploy-kubernetes-dashboard/),
   [Functionalities](https://www.replex.io/blog/how-to-install-access-and-add-heapster-metrics-to-the-kubernetes-dashboard), 
1. [Installing Ingress](https://kubernetes.github.io/ingress-nginx/deploy/) 
1. [Kube Plugins with Krew](https://krew.sigs.k8s.io/plugins/)

## Authentication
If using Password Flow in Postman, make the following settings:
`Postman=>Settings=>General=>SSL certificate verification=>**false**`

* Auth Url: `https://{host}/auth/realms/{finance-tracker}/protocol/openid-connect/auth`
* Token Url: `https://{host}/auth/realms/{finance-tracker}/protocol/openid-connect/token`

Both can be taken from: 
* OIDC Well-known URI Discovery: `https://{host}/auth/realms/{finance-tracker}/.well-known/openid-configuration`

## Deploying in kubernetes   
1. `kubectl apply -f ingress.yaml`
1. `kubectl apply -f postgre-config.yaml`
1. `kubectl apply -f postgres-deployment.yaml`
1. `kubectl apply -f keycloak-config.yaml`
1. `kubectl create configmap keycloak-realm-config --from-file .\realm-config\`
1. `kubectl apply -f keycloak-deployment.yaml`
1. `kubectl apply -f gateway-deployment.yaml`
1. `kubectl apply -f uaa-service-deployment.yaml`

## Starting the project
bootstrap.yml is need to work with k8s configmaps and secrets



