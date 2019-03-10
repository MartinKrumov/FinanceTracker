FROM openjdk:11.0.1-jre-slim-stretch
RUN  apt-get update && apt-get install -y --no-install-recommends netcat-openbsd
ADD ./target/discovery.jar /usr/local/discovery/
EXPOSE 8761
CMD ["java", "-Xmx200m", "-jar", "/usr/local/discovery/discovery.jar"]