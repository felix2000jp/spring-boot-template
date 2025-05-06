FROM eclipse-temurin:21

RUN groupadd spring && useradd -m -g spring spring

ARG JAR_APP=target/*.jar
COPY ${JAR_APP} app.jar

ARG JAR_OTEL=otel/opentelemetry-javaagent.jar
COPY ${JAR_OTEL} opentelemetry-javaagent.jar

USER spring:spring

ENTRYPOINT [ \
    "java", \
    "-javaagent:opentelemetry-javaagent.jar", \
    "-Dotel.instrumentation.logback-appender.experimental.capture-mdc-attributes=*", \
    "-jar", "app.jar" \
]