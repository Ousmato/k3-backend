FROM openjdk:21

WORKDIR /G-scolaire

COPY /target/Gestion-scolaire-0.0.1-SNAPSHOT.jar /G-scolaire/G-scolaire.jar

ENTRYPOINT ["java", "-jar", "/G-scolaire/G-scolaire.jar"]
