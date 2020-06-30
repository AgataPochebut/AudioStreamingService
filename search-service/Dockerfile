FROM java:8-jre

ADD ./target/search-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/search-service.jar"]