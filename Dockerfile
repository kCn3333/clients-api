# Stage 1: Build with dependency cache layer
FROM maven:3.9-eclipse-temurin-24-alpine AS build
WORKDIR /app

# Cache dependencies separately from source code
# This layer rebuilds only when pom.xml changes
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Build application
COPY src ./src
RUN mvn clean package -DskipTests -q

# Stage 2: Runtime - JRE only, non-root user
FROM eclipse-temurin:24-jre-alpine
WORKDIR /app

# Create non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=build /app/target/clients-api-0.0.1-SNAPSHOT.jar app.jar

# Change ownership to non-root user
RUN chown appuser:appgroup app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]