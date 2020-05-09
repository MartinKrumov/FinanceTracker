FROM adoptopenjdk/openjdk11:jre-11.0.7_10-alpine
ADD ./target/discovery.jar /opt/service/
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/opt/service/discovery.jar"]
