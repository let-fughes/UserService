FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY src ./src

RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

RUN groupadd -r spring && useradd -r -g spring spring
RUN chown -R spring:spring /app
USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]