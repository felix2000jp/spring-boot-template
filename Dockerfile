# To use in prod run the command:
# docker run -v /path-to-certs:/certs:ro spring-app:latest

FROM eclipse-temurin:25
RUN groupadd spring && useradd -m -g spring spring

# Uncomment for ready to use development builds
# COPY certs /certs

COPY otel.jar .
COPY target.jar .

USER spring:spring
ENTRYPOINT ["java", "-javaagent:otel.jar", "-jar", "target.jar"]