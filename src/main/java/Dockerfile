# 1. Base image 선택
FROM openjdk:17-jdk-slim

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. JAR 파일을 컨테이너로 복사 (빌드된 JAR 파일의 이름을 app.jar로 설정)
COPY target/app.jar app.jar

# 4. 컨테이너 실행 시 노출할 포트 설정 (예: 8080)
EXPOSE 8080

# 5. Spring Boot 애플리케이션 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]
