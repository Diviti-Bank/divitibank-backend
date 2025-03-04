
FROM ubuntu:latest as build

RUN apt-get update && apt-get install openjdk-21-jdk maven -y

WORKDIR /app/banco


COPY . .  

RUN mvn clean install

FROM openjdk:21-jdk-slim

COPY --from=build /app/banco/target/divitibank-1.0.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]



