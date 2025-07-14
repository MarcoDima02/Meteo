# Multi-stage build per ottimizzare le dimensioni dell'immagine
FROM maven:3.9.9-eclipse-temurin-17 AS build

# Imposta la directory di lavoro
WORKDIR /app

# Copia i file di configurazione Maven
COPY pom.xml .

# Scarica le dipendenze (layer cacheable)
RUN mvn dependency:go-offline

# Copia il codice sorgente
COPY src ./src

# Compila l'applicazione
RUN mvn clean package -DskipTests

# Stage di runtime
FROM eclipse-temurin:17-jre

# Installa curl per health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Crea un utente non-root per sicurezza
RUN groupadd -r meteo && useradd -r -g meteo meteo

# Imposta la directory di lavoro
WORKDIR /app


# Copia il JAR compilato dal stage di build
COPY --from=build /app/target/meteo-app-*.jar app.jar

# Crea la directory dati
RUN mkdir -p /app/data

# Copia lo script di entrypoint
COPY entrypoint.sh /entrypoint.sh
# Fix per line endings Windows/Linux e permessi
RUN sed -i 's/\r$//' /entrypoint.sh && chmod +x /entrypoint.sh

# Espone la porta 8080
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8080/api/weather/italy/all || exit 1

# Comando per avviare l'applicazione tramite entrypoint custom
ENTRYPOINT ["/entrypoint.sh"]

# Opzioni JVM per container
ENV JAVA_OPTS="-Xmx512m -Xms256m"
