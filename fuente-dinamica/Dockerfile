# ==========================
# Etapa 1: Build del JAR con Maven
# ==========================
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# 1. Copia TODO el proyecto al contenedor.
# Esto es necesario para que Maven (el POM padre) pueda:
# a) Entender la estructura multi-módulo.
# b) Resolver las dependencias internas de 'fuente-dinamica' (ej. si depende de 'commons').
COPY . .

# 2. Compila SOLAMENTE el módulo 'fuente-dinamica' y sus dependencias (gracias a -am).
# Maven solo descargará las dependencias necesarias para estos módulos.
RUN mvn clean package -DskipTests -pl fuente-dinamica -am


FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# 3. Copia el JAR compilado desde la carpeta 'target' del módulo 'fuente-dinamica'.
# **IMPORTANTE**: La ruta debe ser /app/[nombre-del-modulo]/target/*.jar
COPY --from=builder /app/fuente-dinamica/target/*.jar app.jar

# Configuración del servicio
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]