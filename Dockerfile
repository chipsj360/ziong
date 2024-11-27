# Use an OpenJDK image as a base
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the application jar file to the container
COPY src/ziong.jar app.jar

# Expose the port your Spring Boot app will run on (typically 80)
EXPOSE 443

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
