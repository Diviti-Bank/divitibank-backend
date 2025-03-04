from ubuntu:latest as build

run apt-get update
run apt-get install openjdk-21-jdk -y
WORKDIR /banco
COPY . .

run apt-get install maven -y
RUN mvn clean install

FROM openjdk:21-jdk-slim

COPY --from=build /banco/target/divitibank-1.0.0.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

