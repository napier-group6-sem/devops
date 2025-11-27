FROM eclipse-temurin:17-jre
WORKDIR /app
COPY target/sem-1.0-SNAPSHOT.jar app.jar
ENV DB_HOST=sem_db
ENV DB_PORT=3306
ENV DB_NAME=world
ENV DB_USER=root
ENV DB_PASS=example
ENTRYPOINT ["java", "-jar", "app.jar"]
