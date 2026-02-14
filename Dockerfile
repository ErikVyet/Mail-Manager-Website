FROM maven:3.9.12-amazoncorretto-25 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:25-alpine
COPY --from=build /target/mail-manager-website-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]