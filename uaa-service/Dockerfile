FROM adoptopenjdk/openjdk11:jre-11.0.7_10-alpine
ADD ./target/uaa-service.jar /opt/service/
EXPOSE 8082
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/opt/service/uaa-service.jar"]
