FROM eclipse-temurin:17-jre-alpine
ADD ./target/uaa-service.jar /opt/service/
EXPOSE 8082
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/opt/service/uaa-service.jar"]
