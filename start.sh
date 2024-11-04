# start.sh
#!/bin/sh

# Nginx 실행
nginx &

# Spring Boot 애플리케이션 실행
java -jar /app/app.jar
