#!/bin/sh
getPort() {
    echo $1 | cut -d : -f 3 | xargs basename
}

while ! `nc -z  discovery $(getPort $DISCOVERY_PORT)`; do sleep 15; done

echo "********************************************************"
echo "Starting Authorization Service"
echo "Using Profile: $SPRING_PROFILES_ACTIVE"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom  \
     -jar /opt/service/authorization-service.jar
