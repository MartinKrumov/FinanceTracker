FROM adoptopenjdk/openjdk11:jre-11.0.7_10-alpine
ADD ./target/gateway.jar /opt/service/
EXPOSE 4000
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/opt/service/gateway.jar"]
