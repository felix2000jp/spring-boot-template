# To use in prod run the command:
# docker run -v /path-to-certs:/certs:ro spring-app:latest

FROM eclipse-temurin:25
RUN groupadd spring && useradd -m -g spring spring

ARG TARGET_JAR=target/*.jar
COPY ${TARGET_JAR} target.jar

USER spring:spring
ENTRYPOINT ["java", "-jar", "target.jar"]
