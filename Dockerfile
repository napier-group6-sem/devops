FROM openjdk:latest
COPY ./target/classes /app
WORKDIR /app
ENTRYPOINT ["java", "com.napier.com.App"]
