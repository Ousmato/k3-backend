FROM openjdk:21

# WORKDIR /G-scolaire

# COPY /target/Gestion-scolaire-0.0.1-SNAPSHOT.war /G-scolaire/G-scolaire.war
# EXPOSE 8080
# ENTRYPOINT ["java", "-jar", "/G-scolaire/G-scolaire.jar"]

# # Utiliser l'image officielle OpenJDK
# FROM openjdk:17-jdk-slim

# Définir le répertoire de travail
WORKDIR /app

# Copier le JAR dans l'image
COPY target/Gestion-scolaire-0.0.1-SNAPSHOT.jar app.jar

# Exposer le port sur lequel l'application tourne
EXPOSE 8080

# Lancer l'application avec le profil "docker"
CMD ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]
