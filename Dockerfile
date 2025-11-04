# ===========================
# Stage 1: Build
# ===========================
FROM eclipse-temurin:17-jdk-alpine AS build

# Installazione di bash, curl e git (necessari per Maven Wrapper)
RUN apk add --no-cache bash curl git

# Imposta la cartella di lavoro
WORKDIR /workspace

# Copia Maven Wrapper e configurazioni
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# ✅ Rende eseguibile il Maven Wrapper
RUN chmod +x mvnw

# Pre-download delle dipendenze per caching più efficiente
RUN ./mvnw -B dependency:go-offline

# Copia il codice sorgente
COPY src src

# Build del JAR senza test (più veloce)
RUN ./mvnw -B clean package -DskipTests

# ===========================
# Stage 2: Runtime
# ===========================
FROM eclipse-temurin:17-jre-alpine

# Cartella di lavoro
WORKDIR /app

# Copia il JAR buildato dallo stage precedente
COPY --from=build /workspace/target/portfolio-backend-0.0.1-SNAPSHOT.jar app.jar

# Espone la porta dell'app
EXPOSE 8080

# Comando di avvio
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
