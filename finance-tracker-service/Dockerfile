FROM openjdk:11.0.1-jre-slim-stretch
RUN  apt-get update && apt-get install -y --no-install-recommends netcat-openbsd
ADD ./target/finance-tracker-service.jar /finance-tracker-service/
ADD run.sh run.sh
RUN chmod +x run.sh
EXPOSE 8090
CMD ./run.sh