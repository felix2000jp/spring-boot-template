FROM eclipse-temurin:25
RUN groupadd spring && useradd -m -g spring spring

ENV CERTS=/certs
ARG OTEL_JAR=otel/*.jar
ARG TARGET_JAR=target/*.jar

COPY ${CERTS} /certs
COPY ${OTEL_JAR} /otel.jar
COPY ${TARGET_JAR} /target.jar

USER spring:spring
ENTRYPOINT ["java", "-javaagent:/otel.jar", "-jar", "/target.jar"]