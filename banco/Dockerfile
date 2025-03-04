# Using a base image with JDK 21 and Maven  
FROM maven:3.8.6-openjdk-21 AS build

# Set as working directory
WORKDIR /banco

# Copy files from your project to the working directory
COPY . /banco

# Run Maven to build the project
RUN mvn package -DskipTests

# Create a new image based on OpenJDK 21 (JRE version for runtime)
FROM openjdk:21-jre-slim-buster


COPY --from=build /banco/target/divitibank-1.0.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

