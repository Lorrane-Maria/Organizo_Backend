# Dockerfile (Versão Otimizada e Segura)

# --- Estágio 1: Build com Maven ---
# Usamos uma imagem que já contém o Maven e o JDK 21
FROM maven:3.9.4-eclipse-temurin-21 AS builder

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia primeiro o pom.xml para aproveitar o cache de dependências do Docker
COPY pom.xml .
# Baixa todas as dependências. Se o pom.xml não mudar, esta camada é reutilizada.
RUN mvn dependency:go-offline

# Agora copia o resto do código fonte
COPY src ./src

# Roda o build do projeto, pulando os testes para um build mais rápido na imagem
RUN mvn clean package -DskipTests

# --- Estágio 2: Imagem Final de Runtime ---
# Usamos uma imagem JRE leve, apenas com o Java Runtime para executar
FROM eclipse-temurin:21-jre

# Cria um usuário e grupo não-root para rodar a aplicação
RUN groupadd -r appgroup && useradd -r -g appgroup appuser

# Define o diretório de trabalho
WORKDIR /app

# Copia o JAR final que foi gerado no estágio 'builder'
# O nome do JAR deve corresponder ao gerado pelo seu pom.xml
COPY --from=builder /app/target/organizobackend-1.0.0.jar app.jar

# Dá a permissão do arquivo JAR para o novo usuário
RUN chown appuser:appgroup app.jar

# Muda para o usuário não-root
USER appuser

# Expõe a porta que a aplicação usa
EXPOSE 8080

# Comando para iniciar a aplicação quando o container for executado
ENTRYPOINT ["java","-jar","app.jar"]