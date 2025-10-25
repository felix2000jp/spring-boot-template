FROM eclipse-temurin:25
RUN groupadd spring && useradd -m -g spring spring

ARG TARGET_JAR=target/*.jar

COPY certs /certs
COPY otel/*.jar /otel.jar
COPY ${TARGET_JAR} /target.jar

USER spring:spring
ENTRYPOINT ["java", "-javaagent:/otel.jar", "-jar", "/target.jar"]