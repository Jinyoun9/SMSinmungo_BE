# 1단계: 프론트엔드 빌드
FROM node:18 AS frontend-build
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm install
COPY frontend ./
RUN npm run build

# 2단계: 백엔드 빌드
FROM gradle:7.6.0-jdk17 AS backend-build
WORKDIR /app/backend
COPY backend .
RUN gradle bootJar

# 3단계: 런타임 이미지 설정
FROM openjdk:17-jdk-alpine
WORKDIR /app

# 프론트엔드 빌드 결과를 Nginx에 복사
COPY --from=frontend-build /app/frontend/build /usr/share/nginx/html

# 백엔드 빌드 결과를 JAR 파일로 복사
COPY --from=backend-build /app/backend/build/libs/*.jar app.jar

# Nginx와 백엔드 서버를 동시에 실행하기 위한 스크립트 작성
COPY start.sh /app/start.sh
RUN chmod +x /app/start.sh

# 80 포트 (프론트엔드)와 8080 포트 (백엔드) 열기
EXPOSE 80 8080

# 시작 스크립트 실행
ENTRYPOINT ["/app/start.sh"]
