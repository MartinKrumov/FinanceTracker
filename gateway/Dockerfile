FROM eclipse-temurin:21.0.2_13-jre-alpine
ADD ./target/gateway.jar /opt/service/
EXPOSE 4000
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/opt/service/gateway.jar"]
