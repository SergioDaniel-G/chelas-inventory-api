FROM eclipse-temurin:17-jdk-alpine
RUN apk add --no-cache curl
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["sh", "-c", "sleep 15 && java -jar app.jar"]