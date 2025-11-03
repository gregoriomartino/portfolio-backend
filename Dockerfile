# Stage 1: Build
FROM eclipse-temurin:17-jdk-alpine AS build

# Install bash e curl (necessari per Maven Wrapper se non già presenti)
RUN apk add --no-cache bash curl git

# Imposta la cartella di lavoro
WORKDIR /workspace

# Copia Maven Wrapper e configurazioni
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Pre-download delle dipendenze per caching più efficiente
RUN ./mvnw -B dependency:go-offline

# Copia il codice sorgente
COPY src src

# Build del JAR senza test
RUN ./mvnw -B clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre-alpine

# Cartella di lavoro
WORKDIR /app

# Copia il JAR buildato dal stage precedente
COPY --from=build /workspace/target/portfolio-backend-0.0.1-SNAPSHOT.jar app.jar

# Espone la porta dell'app
EXPOSE 8080

# Comando di avvio
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
