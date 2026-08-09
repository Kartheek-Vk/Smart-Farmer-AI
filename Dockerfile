# syntax=docker/dockerfile:1

FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml ./
RUN --mount=type=cache,target=/root/.m2 mvn -B dependency:go-offline
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -B -DskipTests package && cp target/*.jar /workspace/app.jar

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
RUN useradd --system --create-home --uid 10001 smartfarmer
COPY --from=build /workspace/app.jar /app/app.jar
RUN mkdir -p /app/uploads && chown -R smartfarmer:smartfarmer /app
USER smartfarmer
EXPOSE 8080
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
