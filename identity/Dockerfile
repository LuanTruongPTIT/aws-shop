FROM eclipse-temurin:21-jre-alpine
COPY target/identity*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]