from ubuntu:latest as build

run apt-get update
run apt-get install openjdk-21-jdk -y
COPY . .

run apt-get install maven -y


FROM openjdk:21-jdk-slim

COPY --from=build /app/target/divitibank.jar app.jar

CMD ["java", "-jar", "app.jar"]

