# Build stage
FROM maven:3.9.9-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -e -DskipTests=true package

# Runtime stage
FROM tomcat:9.0-jdk11
# Remove default apps
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=build /app/target/bus-reservation.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
