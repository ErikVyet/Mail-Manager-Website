FROM maven:3.9.9-eclipse-temurin-25 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:25-jre-jammy
COPY --from=build /target/mail-manager-website-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]