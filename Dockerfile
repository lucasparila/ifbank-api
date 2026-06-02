# Estágio 1: Build da aplicação usando o Maven Wrapper
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app

# Copia os arquivos de configuração do Maven do projeto
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Altera a permissão do wrapper para execução (essencial para Linux/Mac)
RUN chmod +x mvnw

# Baixa as dependências do Maven (cache)
RUN ./mvnw dependency:go-offline

# Copia o código fonte e gera o arquivo .jar final
COPY src ./src
RUN ./mvnw clean package -DskipTests

# Estágio 2: Execução com um JRE mais leve
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copia o .jar gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring
EXPOSE 8080

# Comando para rodar a API
ENTRYPOINT ["java", "-jar", "app.jar"]