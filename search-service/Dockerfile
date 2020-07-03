FROM openjdk:11

ADD ./target/search-service.jar /app/
CMD ["java", "-Xmx200m", "-jar", "/app/search-service.jar"]