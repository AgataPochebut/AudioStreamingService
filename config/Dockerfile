FROM java:8-jre

ADD ./target/*.war /app/config.war
#ENTRYPOINT ["php", "/cli.php"]
CMD ["java", "-Xmx200m", "-jar", "/app/config.war"]

HEALTHCHECK --interval=30s --timeout=30s CMD curl -f http://localhost:8888/actuator/health || exit 1

EXPOSE 8888