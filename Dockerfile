FROM eclipse-temurin:21

RUN groupadd spring && useradd -m -g spring spring

ARG APP_JAR=target/*.jar
COPY ${APP_JAR} app.jar

ARG OTEL_JAR=otel/opentelemetry-javaagent.jar
COPY ${OTEL_JAR} opentelemetry-javaagent.jar

USER spring:spring

ENTRYPOINT [ \
    "java", \
    "-javaagent:opentelemetry-javaagent.jar", \
    "-Dotel.instrumentation.logback-appender.experimental.capture-mdc-attributes=*", \
    "-jar", "app.jar" \
]