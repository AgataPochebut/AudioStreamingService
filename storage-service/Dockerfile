FROM java:8-jre

ADD ./target/storage-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/storage-service.jar"]