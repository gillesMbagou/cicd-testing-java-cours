# Utilisez l'image officielle Tomcat avec la version souhaitée
FROM eclipse-temurin:17-jre-alpine

ARG JAR_FILE=target/calculator.jar

WORKDIR /opt/app

# Copier le fichier JAR dans le conteneur
COPY ${JAR_FILE} calculator.jar

COPY entrypoint.sh entrypoint.sh
RUN ls -l && chmod 755 entrypoint.sh

EXPOSE 8081

# Définir le point d'entrée pour le conteneur
ENTRYPOINT ["/opt/app/entrypoint.sh"]

