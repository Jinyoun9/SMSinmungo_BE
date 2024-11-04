# Build 단계
FROM gradle:jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build -x test

# Run 단계
FROM openjdk:17-jdk-slim
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
