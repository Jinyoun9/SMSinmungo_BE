# Build 단계
FROM gradle:jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build -x test

# 로컬에서 빌드 후, 빌드된 JAR 파일을 Docker 이미지로 복사하는 예제
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY build/libs/your-app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
