FROM eclipse-temurin:21

RUN groupadd spring && useradd -m -g spring spring
USER spring:spring

ARG OTEL_VER=v2.14.0
ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/${OTEL_VER}/opentelemetry-javaagent.jar /opentelemetry-javaagent.jar

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT [ \
    "java", \
    "-javaagent:opentelemetry-javaagent.jar", \
    "-Dotel.instrumentation.logback-appender.experimental.capture-mdc-attributes=*", \
    "-jar", "app.jar" \
]