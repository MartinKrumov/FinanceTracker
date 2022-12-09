FROM eclipse-temurin:17.0.3_7-jre-alpine
ADD ./target/finance-tracker-service.jar /opt/service/
EXPOSE 8090
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/opt/service/finance-tracker-service.jar"]
