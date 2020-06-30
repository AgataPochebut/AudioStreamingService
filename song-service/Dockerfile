FROM java:8-jre

ADD ./target/song-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/song-service.jar"]