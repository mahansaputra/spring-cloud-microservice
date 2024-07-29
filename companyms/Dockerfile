# Stage 1: Build the application
FROM maven:3.9.8-eclipse-temurin-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and the src directory to the working directory
COPY pom.xml .
COPY src ./src

# Clean and package the application
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17

# Set the working directory inside the container
WORKDIR /app

# Copy the packaged jar file from the build stage
COPY --from=build /app/target/companyms-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8081

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
