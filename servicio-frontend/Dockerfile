
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests -pl servicio-frontend -am


FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=builder /app/servicio-frontend/target/*.jar app.jar

EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]