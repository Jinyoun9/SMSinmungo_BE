# 첫 번째 단계: 빌드
FROM gradle:7.5.1-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build -x test   # 테스트 생략하고 빌드 진행

# 두 번째 단계: 실행
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
