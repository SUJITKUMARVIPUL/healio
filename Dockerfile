# Stage 1: Build the application jar
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
WORKDIR /app
COPY pom.xml .
# Download dependencies first to leverage Docker layer caching
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copy the built jar from Stage 1 cleanly
COPY --from=build /app/target/healio-0.0.1-SNAPSHOT.jar healio.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "healio.jar"]