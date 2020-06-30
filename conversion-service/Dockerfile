FROM java:8-jre

ADD ./target/conversion-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/conversion-service.jar"]