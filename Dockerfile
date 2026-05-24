FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/child_tracking-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/firebase-service-account.json ./firebase-service-account.json
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]