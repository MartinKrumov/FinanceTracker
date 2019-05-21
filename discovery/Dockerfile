FROM openjdk:11.0.1-jre-slim-stretch
ADD ./target/discovery.jar /opt/service/
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/opt/service/discovery.jar"]
