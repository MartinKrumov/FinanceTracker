FROM openjdk:11.0.1-jre-slim-stretch
ADD ./target/discovery.jar /usr/local/discovery/
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/usr/local/discovery/discovery.jar"]
