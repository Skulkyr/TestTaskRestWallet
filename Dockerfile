FROM maven:openjdk as builder
WORKDIR /opt/app
COPY mvnw pom.xml ./
COPY ./src ./src
RUN mvn clean install -DskipTests

FROM openjdk:17
WORKDIR /opt/app
COPY --from=builder opt/app/target/*.jar opt/app/*.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "opt/app/*.jar"]