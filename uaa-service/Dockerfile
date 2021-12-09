FROM eclipse-temurin:17-jdk-alpine
ADD ./target/uaa-service.jar /opt/service/
EXPOSE 8082
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/opt/service/uaa-service.jar"]
