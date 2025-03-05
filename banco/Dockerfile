FROM ubuntu:latest as build

RUN apt-get update && apt-get install openjdk-21-jdk maven -y

WORKDIR /banco

COPY ./banco .

run mvn clean package -DskipTests -X

FROM openjdk:21-jdk-slim

WORKDIR /banco

COPY --from=build /target/divitibank-1.0.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]


