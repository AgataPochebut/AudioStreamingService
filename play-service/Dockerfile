FROM openjdk:11

COPY ./wait-for-it.sh wait-for-it.sh
RUN chmod +x /wait-for-it.sh

ADD ./target/play-service.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]