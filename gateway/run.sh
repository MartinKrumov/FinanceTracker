#!/bin/sh
getPort() {
  echo $1 | cut -d : -f 3 | xargs basename
}

echo "********************************************************"
echo "Waiting for Discovery server $(getPort $DISCOVERY_PORT)"
echo "********************************************************"
while ! `nc -z  discovery $(getPort $DISCOVERY_PORT)`; do sleep 20; done
echo "******* Discovery server has started"

echo "********************************************************"
echo "Starting API Gateway"
echo "Using Profile: $SPRING_PROFILES_ACTIVE"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom  \
     -jar /opt/service/gateway.jar
