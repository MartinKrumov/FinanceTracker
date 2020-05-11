FROM adoptopenjdk/openjdk11:jre-11.0.7_10-alpine
ADD ./target/finance-tracker-service.jar /opt/service/
EXPOSE 8090
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/opt/service/finance-tracker-service.jar"]
