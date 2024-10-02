# Stage 1: Build the JAR file
FROM maven:3.8.7-openjdk-18-slim AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files to the container
# TODO : Can i get this code directly from github source code ?
COPY pom.xml .
COPY src ./src

# Build the application using Maven
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy only the built JAR file from the previous stage
# TODO : Declare this as variable & use it
COPY --from=build /app/target/spring-auth-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port your application will run on
EXPOSE 8080

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
