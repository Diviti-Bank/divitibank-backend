# Etapa de build
FROM maven:3.8.6-openjdk-21 AS build
WORKDIR /app
COPY . .
RUN mvn package -DskipTests

# Etapa final
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]

