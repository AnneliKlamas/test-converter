FROM openjdk:21

RUN mkdir -p /app
COPY target/converter-0.0.1-SNAPSHOT.jar /app

EXPOSE 8000
CMD ["java", "-jar", "/app/converter-0.0.1-SNAPSHOT.jar"]
