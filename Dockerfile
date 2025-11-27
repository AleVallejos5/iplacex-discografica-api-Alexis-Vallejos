# Stage 1: Build con Gradle - versión más actualizada
FROM gradle:8.14.0-jdk17 AS builder



WORKDIR /app

# Copiar archivos de configuración primero (para mejor cache)
COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle gradle

# Descargar dependencias (cache layer)
RUN ./gradlew dependencies --no-daemon

# Copiar código fuente
COPY src src

# Dar permisos y construir
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:17-jre-alpine
 
WORKDIR /app

# Copiar el JAR
COPY --from=builder /app/build/libs/*.jar app.jar
 
EXPOSE 8080
 
ENTRYPOINT ["java", "-jar", "app.jar"]