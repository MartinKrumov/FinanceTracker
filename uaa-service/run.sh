#!/bin/sh
getPort() {
    echo $1 | cut -d : -f 3 | xargs basename
}

while ! `nc -z  discovery $(getPort $DISCOVERY_PORT)`; do sleep 20; done

echo "********************************************************"
echo "Starting UAA Service"
echo "Using Profile: $SPRING_PROFILES_ACTIVE"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom  \
     -jar /opt/service/uaa-service.jar
