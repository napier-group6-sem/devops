FROM mysql:8.0
COPY world.sql /docker-entrypoint-initdb.d/
ENV MYSQL_ROOT_PASSWORD=example
ENV MYSQL_DATABASE=world
ENV MYSQL_ROOT_HOST=%
