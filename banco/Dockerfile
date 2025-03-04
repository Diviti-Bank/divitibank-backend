FROM ubuntu:latest as build
RUN apt-get update && apt-get install openjdk-21-jdk maven -y

# Define o diretório de trabalho
WORKDIR /app/banco

# Copia apenas o pom.xml primeiro para aproveitar o cache do Docker
COPY pom.xml .

# Baixa as dependências (isso pode ser armazenado em cache)
RUN mvn dependency:go-offline

# Copia o restante do código
COPY . .

# Executa o build do Maven
RUN mvn clean install

FROM openjdk:21-jdk-slim

# Copia o JAR gerado para o contêiner final
COPY --from=build /app/banco/target/divitibank-1.0.0.jar app.jar

# Define o comando de execução
ENTRYPOINT ["java", "-jar", "app.jar"]

