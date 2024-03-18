FROM eclipse-temurin:21.0.2_13-jre-alpine
ADD ./target/finance-tracker-service.jar /opt/service/
EXPOSE 8090
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/opt/service/finance-tracker-service.jar"]
