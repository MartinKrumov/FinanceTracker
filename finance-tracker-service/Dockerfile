FROM adoptopenjdk/openjdk11:jre-11.0.7_10-alpine
RUN apk update && apk upgrade && apk add netcat-openbsd
ADD ./target/finance-tracker-service.jar /opt/service/
#ADD run.sh run.sh
#RUN chmod +x run.sh
EXPOSE 8090
#ENTRYPOINT ./run.sh
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/opt/service/finance-tracker-service.jar"]
