# Finance Tracker

## Structure 
Each maven module in the root project is a single microservice.

## Requirements
To run the example applications you need to have installed:
1. JDK 17
2. Maven (Optional)
3. Docker
4. Kubernetes 1.22+

## Usage 
1. To build all microservices run the command `./mvnw clean install`
1. To run all services [Follow the guide](#deploying-in-kubernetes)

## Deployment
For local deployment, install dashboard and ingress:

**Installing Kubernetes:**
1. [On WSL2 - KinD or Minikube](https://kubernetes.io/blog/2020/05/21/wsl-docker-kubernetes-on-the-windows-desktop/)
2. [Dashboard Official Repository](https://github.com/kubernetes/dashboard),
   [Dashboard Setup](https://upcloud.com/community/tutorials/deploy-kubernetes-dashboard/),
   [Functionalities](https://www.replex.io/blog/how-to-install-access-and-add-heapster-metrics-to-the-kubernetes-dashboard), 
   1. List token `kubectl describe secret admin-user-dashboard -n kubernetes-dashboard`
   2. Create token `kubectl -n kubernetes-dashboard create token admin-user --duration=99999h`
3. [Installing Ingress](https://kubernetes.github.io/ingress-nginx/deploy/) 
4. [Kube Plugins with Krew](https://krew.sigs.k8s.io/plugins/)

## Authentication
If using Password Flow in Postman, make the following settings:
`Postman=>Settings=>General=>SSL certificate verification=>**false**`

* Auth Url: `https://{host}/auth/realms/{finance-tracker}/protocol/openid-connect/auth`
* Token Url: `https://{host}/auth/realms/{finance-tracker}/protocol/openid-connect/token`

Both can be taken from: 
* OIDC Well-known URI Discovery: `https://{host}/auth/realms/{finance-tracker}/.well-known/openid-configuration`

## Deploying in kubernetes   
1. Ingress - `kubectl apply -f ingress.yaml`
2. Cluster Role(Permissions) - `kubectl apply -f cluster-role-binding.yaml`
3. PostgreSQL
   1. `kubectl apply -f postgre-config.yaml`
   2. `kubectl apply -f postgres-deployment.yaml`
4. Keycloak:
   1. `kubectl apply -f keycloak-config.yaml`
   2. `kubectl create configmap keycloak-realm-config --from-file .\realm-config\`
   3. `kubectl apply -f keycloak-deployment.yaml`
5. Config Watcher - `kubectl apply -f config-watcher.yaml`
6. Gateway - `kubectl apply -f gateway-deployment.yaml`
7. UAA service -`kubectl apply -f uaa-service-deployment.yaml`
8. FT Service
   1. `kubectl apply -f ft-tracker-config.yaml`
   1. `kubectl apply -f finance-tracker-service-deployment.yaml`

## Starting the project
bootstrap.yml 
1. is need to work with k8s configmaps and secrets
2. to run locally disable kubernetes in 'bootstrap.yml'




