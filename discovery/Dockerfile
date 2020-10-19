FROM openjdk:11

ADD ./target/discovery.jar app.jar
CMD ["java", "-Xmx200m", "-jar", "/app.jar"]

EXPOSE 8761