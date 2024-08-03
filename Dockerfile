# Use an official OpenJDK runtime as a parent image
FROM openjdk:24-jdk-slim-bookworm

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the current directory to the container
COPY StudentsViolation-0.0.1-SNAPSHOT.jar /app/studentsviolation.jar

# Run the JAR file
CMD ["java", "-jar", "/app/studentsviolation.jar"]
