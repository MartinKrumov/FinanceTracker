#!/bin/sh
getPort() {
    echo $1 | cut -d : -f 3 | xargs basename
}

echo "******************************************************"
echo "Waiting for Database to start on port: $(getPort $DB_PORT)"
echo "******************************************************"
while ! nc -z postgres $(getPort $DB_PORT); do sleep 20; done
echo "***** Database has started"

echo "******************************************************"
echo "Finance Tracker Service"
echo "Using Profile: $SPRING_PROFILES_ACTIVE"
echo "******************************************************"
java -Djava.security.egd=file:/dev/./urandom  \
     -jar /opt/service/finance-tracker-service.jar
