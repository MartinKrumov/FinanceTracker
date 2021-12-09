FROM eclipse-temurin:17-jdk-alpine
ADD ./target/discovery.jar /opt/service/
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/opt/service/discovery.jar"]
