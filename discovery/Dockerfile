FROM openjdk:11

#ADD ./init.sql /sql/discovery.sql
ADD ./target/discovery.jar /app/

#CMD ["java", "-Xmx200m", "-jar", "/app/discovery.sql"]
CMD ["java", "-Xmx200m", "-jar", "/app/discovery.jar"]

EXPOSE 8761