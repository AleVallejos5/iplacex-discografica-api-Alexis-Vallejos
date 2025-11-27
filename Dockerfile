# Stage 1: Build con Gradle y JDK 21
FROM gradle:8.10.2-jdk21-alpine AS builder

WORKDIR /app
COPY . .
RUN gradle clean build -x test --no-daemon

# Stage 2: Runtime con JRE 21
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/build/libs/discografia-1.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]