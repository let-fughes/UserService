FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/*.jar app.jar

RUN groupadd -r spring && useradd -r -g spring spring
RUN chown -R spring:spring /app
USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]