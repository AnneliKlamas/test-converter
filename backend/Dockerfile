FROM maven:3.9.6-eclipse-temurin-21-alpine

RUN mkdir -p /app
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY . .
RUN mvn package

EXPOSE 8000
CMD ["java", "-jar", "target/converter-0.0.1-SNAPSHOT.jar"]
