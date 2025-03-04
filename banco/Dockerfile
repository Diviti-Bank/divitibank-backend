from ubunto:latest as build

run apt-get update
run apt-get install openjdk-21-jdk -y
COPY . .

run apt-get install maven -y
run mvn clean install

FROM openjdk:21-jdk-slim

COPY --from=build /app/target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]

