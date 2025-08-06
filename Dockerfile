# Usa un'immagine con Java 21 JDK
FROM eclipse-temurin:21-jdk

# Directory di lavoro all'interno del container
WORKDIR /app

# Copia il file JAR nella cartella /app del container
COPY target/steam-clone-backend-0.0.1-SNAPSHOT.jar app.jar

# Espone la porta
EXPOSE 8080

# Comando per avviare l'app Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]