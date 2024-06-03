# Use the official Maven image to create a build artifact.
FROM maven:3.9.4-eclipse-temurin-17 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml file to download dependencies
COPY pom.xml .

# Download dependencies as a separate step to take advantage of Docker caching
RUN mvn dependency:go-offline

# Copy the rest of the source code
 COPY src ./src 

# Build a release artifact
 RUN mvn clean package 

# Use Eclipse Temurin for runtime
FROM eclipse-temurin:17-jdk

# Set the working directory
WORKDIR /app

# Copy the built artifact from the previous stage
COPY --from=build /app/target/G1Server-0.0.1-SNAPSHOT.jar ./G1Server.jar

# Specify the command to run on container start
#CMD ["java", "-jar", "./G1Server.jar"]
# As a placeholder, just keep the container running (instead of running a JAR)
#CMD ["tail", "-f", "/dev/null"]