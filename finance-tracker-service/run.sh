#!/bin/sh
getPort() {
    echo $1 | cut -d : -f 3 | xargs basename
}

echo "******************************************************"
echo "Waiting for Postgres $(getPort $POSTGRES_PORT)"
echo "******************************************************"
while ! nc -z postgres $(getPort $POSTGRES_PORT); do sleep 3; done
echo "***** Postgres has started"

echo "******************************************************"
echo "Finance Tracker Service"
echo "Using Profile: $PROFILE"
echo "******************************************************"
java -Djava.security.egd=file:/dev/./urandom  \
     -Dspring.profiles.active=$PROFILE                          \
     -jar /finance-tracker-service/finance-tracker-service.jar