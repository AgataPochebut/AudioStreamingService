FROM openjdk:11

RUN apt-get clean && apt-get update
RUN apt-get install -y ffmpeg

COPY ./wait-for-it.sh wait-for-it.sh
RUN chmod +x /wait-for-it.sh

ADD ./target/conversion-service.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]