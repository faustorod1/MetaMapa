
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests -pl servicio-de-usuarios -am


FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=builder /app/servicio-de-usuarios/target/*.jar app.jar

EXPOSE 8086
ENTRYPOINT ["java", "-jar", "app.jar"]