FROM maven:3.9.6-eclipse-temurin-21 AS build

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

