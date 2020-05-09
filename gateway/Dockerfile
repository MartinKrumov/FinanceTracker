FROM adoptopenjdk/openjdk11:jre-11.0.7_10-alpine
RUN apk update && apk upgrade && apk add netcat-openbsd
ADD ./target/gateway.jar /opt/service/
ADD run.sh run.sh
RUN chmod +x run.sh
EXPOSE 4000
CMD ./run.sh
