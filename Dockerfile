# Dockerfile to Build and Package Your Spring Boot Application with Artifacts from Nexus Repository

# Step 1: Use an official OpenJDK runtime as the base image
FROM openjdk:17-jdk-slim

# Step 2: Set the working directory inside the container
WORKDIR /app

# Step 3: Install curl to download artifacts from Nexus repository
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Step 4: Set environment variables for Nexus repository details
ARG NEXUS_URL=http://192.168.33.11:8081/repository
ARG NEXUS_REPO=maven-releases
ARG GROUP_ID=com/supportportal/supportportal
ARG VERSION=0.0.1

# Step 5: Construct the Nexus URL for the artifact
RUN curl -o app.jar "$NEXUS_URL/$NEXUS_REPO/$(echo $GROUP_ID | tr '.' '/')/$VERSION/supportportal-$VERSION.jar"

# Step 6: Expose the port your Spring Boot app runs on (8089 in this case)
EXPOSE 8089

# Step 7: Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
