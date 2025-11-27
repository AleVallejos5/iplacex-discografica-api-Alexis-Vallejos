# Stage 1: Build con Gradle
FROM gradle:7.4.2-jdk17 AS builder

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos necesarios para cache de dependencias
COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle gradle
COPY src src

# Dar permisos apropiados para gradle
RUN chmod +x ./gradlew

# Construir el JAR (excluyendo tests para mayor velocidad)
RUN gradle build -x test --no-daemon

# Stage 2: Ejecución
FROM eclipse-temurin:17-jre-alpine

# Instalar dependencias básicas si es necesario
RUN apt-get update && apt-get install -y --no-install-recommends \
    && rm -rf /var/lib/apt/lists/*

# Establecer directorio de trabajo
WORKDIR /app

# Copiar el JAR desde el stage de build
COPY --from=builder /app/build/libs/*.jar app.jar

# Exponer puerto (Render usa puerto 8080 por defecto)
EXPOSE 8080

# Comando de ejecución
ENTRYPOINT ["java", "-jar", "app.jar"]