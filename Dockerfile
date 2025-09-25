# Dockerfile
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Копируем только необходимые файлы для кэширования зависимостей
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY src ./src

# Собираем приложение
RUN ./mvnw clean package -DskipTests

# Финальный образ
FROM eclipse-temurin:21-jre

WORKDIR /app

# Копируем собранный JAR из стадии сборки
COPY --from=build /app/target/*.jar app.jar

# Создаем не-root пользователя для безопасности
RUN groupadd -r spring && useradd -r -g spring spring
RUN chown -R spring:spring /app
USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]