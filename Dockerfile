# Use a base image with JDK 23
FROM eclipse-temurin:23-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/*.jar /app/app.jar
#For Maven
# COPY build/libs/*.jar /app/app.jar # For Gradle

# Expose the port your application runs on (usually 8080)
EXPOSE 8080

# Set the entry point for the container
ENTRYPOINT ["java", "-jar", "app.jar"]