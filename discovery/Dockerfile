FROM eclipse-temurin:21.0.2_13-jre-alpine
ADD ./target/discovery.jar /opt/service/
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/opt/service/discovery.jar"]
