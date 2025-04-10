# Force AMD64 platform for Render compatibility
FROM --platform=linux/amd64 eclipse-temurin:21-jdk-jammy

# Set working directory
WORKDIR /app

# Copy the JAR file (change name if needed)
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Expose port (must match server.port in application.properties)
EXPOSE 8080

# Run the application with DB environment variables
ENTRYPOINT ["java", "-jar", "app.jar"]