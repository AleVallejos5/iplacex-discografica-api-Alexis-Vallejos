# Stage 1: Build
FROM gradle:8.4.0-jdk17 AS builder

WORKDIR /app
COPY . .
RUN chmod +x ./gradlew && ./gradlew clean build -x test

# Stage 2: Run  
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app


COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]