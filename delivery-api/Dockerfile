# 🏗️ Estágio 1: Build (Usando imagem que suporta Java 21)
FROM maven:3.9.6-amazoncorretto-21 AS build
WORKDIR /app

# Copia o pom.xml e baixa dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte
COPY src ./src

# Compila com as flags de correção já validadas
RUN mvn clean package -DskipTests -Dmaven.gitcommitid.skip=true

# 🐳 Estágio 2: Final (Imagem leve para execução)
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copia o JAR gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

# 🔧 Configurações
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]