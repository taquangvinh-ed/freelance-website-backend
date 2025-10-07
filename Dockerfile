FROM openjdk:23-jdk-slim
# Set working directory
WORKDIR /app

# Copy the built JAR file into the container
COPY build/libs/freelancer-backend-0.0.1-SNAPSHOT.jar app.jar

# Set environment variables (overridden by docker-compose.yml)
ENV SPRING_REDIS_HOST=redis
ENV SPRING_REDIS_PORT=6379

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]