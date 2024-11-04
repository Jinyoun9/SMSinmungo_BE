# frontend-build 단계
FROM node:18 AS frontend-build
WORKDIR /app/frontend

# 프론트엔드 경로에 맞춰 파일 복사
COPY smsinmungo/package*.json ./
RUN npm install
COPY smsinmungo/src ./  # 전체 프론트엔드 소스 복사
RUN npm run build

# backend-build 단계
FROM gradle:7.6.0-jdk17 AS backend-build
WORKDIR /app/backend

# 백엔드 경로에 맞춰 파일 복사
COPY src/main/java .  # 백엔드 소스 복사
RUN gradle bootJar

# 런타임 이미지
FROM openjdk:17-jdk-alpine
WORKDIR /app

# 빌드 결과물 복사
COPY --from=frontend-build /app/frontend/build /usr/share/nginx/html
COPY --from=backend-build /app/backend/build/libs/*.jar app.jar

# Nginx와 백엔드를 동시에 실행하는 스크립트 복사
COPY start.sh /app/start.sh
RUN chmod +x /app/start.sh

# 포트 열기
EXPOSE 80 8080

# 애플리케이션 실행
ENTRYPOINT ["/app/start.sh"]
