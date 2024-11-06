FROM maven:3-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

RUN mvn package

FROM eclipse-temurin:21

COPY --from=build ./app/target/nosql.quickstart-0.0.1-SNAPSHOT.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app.jar"]