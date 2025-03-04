# Using Maven 3.9.6 with Eclipse Temurin 21 as the base image for build
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set the working directory inside the container
WORKDIR /banco

# Copy all files from the local project into the working directory
COPY ./banco /banco

# Run Maven to build the project
run mvn clean package -DskipTests -X


# Use OpenJDK 21 JDK slim for runtime image (includes full JDK)
FROM openjdk:21-jdk-slim



    COPY --from=build /target/divitibank-1.0.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

