# Estágio de Build
FROM ubuntu:latest as build

# Atualiza pacotes e instala o OpenJDK e Maven
RUN apt-get update && apt-get install openjdk-21-jdk maven -y

# Define o diretório de trabalho
WORKDIR /app/banco

# Copia todos os arquivos (incluindo o pom.xml) para a imagem
COPY . .  # Certifique-se de que o pom.xml está no diretório atual antes de executar o build

# Executa o Maven para compilar o projeto e gerar o .jar
RUN mvn clean install

# Estágio de Execução
FROM openjdk:21-jdk-slim

# Copia o arquivo .jar gerado no estágio anterior para o estágio final
COPY --from=build /app/banco/target/divitibank-1.0.0.jar app.jar

# Define o comando para rodar o .jar
ENTRYPOINT ["java", "-jar", "app.jar"]



