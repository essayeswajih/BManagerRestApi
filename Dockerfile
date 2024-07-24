FROM maven:3.9.8-amazoncorretto-21-al2023 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
COPY --from=build /target/GestionFactureAPI-0.0.1-SNAPSHOT.jar GestionFactureAPI.jar
EXPOSE 5000
ENTRYPOINT [ "java","-jar","GestionFactureAPI.jar" ]