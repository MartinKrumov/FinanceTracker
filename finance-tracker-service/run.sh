#!/bin/sh
getPort() {
    echo $1 | cut -d : -f 3 | xargs basename
}

echo "******************************************************"
echo "Waiting for Database to start on port: $(getPort $DB_PORT)"
echo "******************************************************"
while ! nc -z postgres $(getPort $DB_PORT); do sleep 3; done
echo "***** Database has started"

echo "******************************************************"
echo "Finance Tracker Service"
echo "Using Profile: $PROFILE"
echo "******************************************************"
java -Djava.security.egd=file:/dev/./urandom  \
     -Dspring.profiles.active=$PROFILE                          \
     -jar /finance-tracker-service/finance-tracker-service.jar