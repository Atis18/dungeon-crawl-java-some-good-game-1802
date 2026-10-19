# Reproducible Maven build and unit tests for the desktop application.
# This image does not launch the JavaFX graphical window.
FROM maven:3.9-eclipse-temurin-17

WORKDIR /workspace
COPY pom.xml ./
COPY src/ ./src/

RUN mvn -B -ntp clean verify

CMD ["mvn", "-B", "-ntp", "verify"]