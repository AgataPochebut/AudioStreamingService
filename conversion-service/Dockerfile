FROM openjdk:11

ADD ./target/conversion-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/conversion-service.jar"]