FROM openjdk:11

ADD ./target/config.jar app.jar
CMD ["java", "-Xmx200m", "-jar", "/app.jar"]

#HEALTHCHECK --interval=30s --timeout=30s CMD curl -f http://localhost:8888/actuator/health || exit 1

EXPOSE 8888