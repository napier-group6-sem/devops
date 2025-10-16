
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/*.jar app.jar
CMD ["java", "-cp", "/app/app.jar", "com.napier.sem.App"]
