FROM eclipse-temurin:17-jre-alpine
ADD ./target/gateway.jar /opt/service/
EXPOSE 4000
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/opt/service/gateway.jar"]
