# Estágio de Build
FROM ubuntu:latest as build

# Atualiza pacotes e instala o OpenJDK e Maven
RUN apt-get update && apt-get install openjdk-21-jdk maven -y

# Define o diretório de trabalho
WORKDIR /banco

# Copia o código fonte para a imagem
COPY . .

# Executa o Maven para compilar o projeto e gerar o .jar
RUN mvn clean install

# Estágio de Execução
FROM openjdk:21-jdk-slim

# Copia o arquivo .jar gerado no estágio anterior para o estágio final
COPY --from=build /banco/target/divitibank-1.0.0.jar app.jar

# Define o comando para rodar o .jar
ENTRYPOINT ["java", "-jar", "app.jar"]

