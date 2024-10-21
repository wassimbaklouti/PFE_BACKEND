# Dockerfile to Build and Package Your Spring Boot Application

# Step 1: Use an official OpenJDK runtime as the base image
FROM openjdk:17-jdk-slim

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Copy the Spring Boot JAR file into the container
# Assumes the JAR file is built using Maven and is in the 'target' directory
COPY target/*.jar app.jar

# Step 4: Expose the port your Spring Boot app runs on (typically 8080)
EXPOSE 8089

# Step 5: Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
