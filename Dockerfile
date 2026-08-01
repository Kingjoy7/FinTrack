# Multi-stage build for a small, production-ready Spring Boot image
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copy the Maven build definition first to leverage Docker layer caching
COPY pom.xml ./
COPY mvnw ./
COPY .mvn ./.mvn

# Download dependencies before copying the full source tree
RUN chmod +x mvnw && ./mvnw -q -DskipTests dependency:go-offline

# Copy the application source and build the JAR
COPY src ./src
RUN ./mvnw -q -DskipTests package

# Runtime stage using a slim JRE image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Create a non-root user for better container security
RUN groupadd --system spring && useradd --system --gid spring spring

# Copy only the built artifact to keep the final image small
COPY --from=build /workspace/target/*.jar app.jar

# Expose the application port used by Spring Boot
EXPOSE 8080

# Run the application as a non-root user
USER spring
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
