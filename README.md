# Spring Boot Template

A Java 25 / Spring Boot template built as a modular monolith, with PostgreSQL,
Flyway migrations, HTTP Basic and JWT authentication, and an example notes API.

## Project structure

Application code lives under `dev.felix2000jp.springboottemplate`:

| Module | Responsibility |
| --- | --- |
| `appusers` | Registration, current-user management, credentials, and token issuance |
| `notes` | Notes belonging to the current user |
| `system` | Shared security services, configuration, and unexpected-error handling |
| `sample` | Empty domain/application/infrastructure packages for a new module |

Feature modules use three layers:

- `domain`: aggregates, value objects, repository interfaces, and domain events.
- `application`: use cases, DTOs, mapping, and event listeners.
- `infrastructure`: HTTP controllers and persistence/security adapters.

Spring Modulith verifies module boundaries in `ApplicationTest`. Cross-module
access uses exposed module APIs and named interfaces, such as the shared security
API and `AppuserDeletedEvent`. The layers have jMolecules onion architecture
annotations, with its ArchUnit integration included in the test dependencies.

Deleting an app user publishes a domain event. The notes module handles it
asynchronously through Spring Modulith and removes that user's notes.

## Run locally

Requirements:

- JDK 25.
- Docker with Docker Compose support. Integration tests also use Docker through
  Testcontainers.

From the project root:

```sh
./mvnw spring-boot:run
```

Spring Boot's Docker Compose integration starts the PostgreSQL service defined in
`compose.yaml`. The local database is `application-db`, with username and password
`postgres`, exposed on port 5432. Flyway applies the migrations from
`src/main/resources/db/migration`.

The application uses port 8080 by default:

- Swagger UI: <http://localhost:8080/swagger-ui/index.html>
- OpenAPI document: <http://localhost:8080/v3/api-docs>
- Health: <http://localhost:8080/actuator/health>

To manage the database yourself, disable Compose integration with
`SPRING_DOCKER_COMPOSE_ENABLED=false` and configure the datasource. Database and
Flyway credentials are configured separately in `application.yaml`; override both
when using different credentials.

## Authentication and authorization

The API is stateless and supports HTTP Basic and JWT Bearer authentication. Server
sessions are disabled, and CSRF protection is disabled in the API filter chain.
There is no frontend session authentication in this template.

Authorization is declared on controller methods with `@PreAuthorize`. The filter
chain permits requests through to those method checks. Add an explicit policy to
every new protected endpoint.

| Endpoint | Authentication and authorization |
| --- | --- |
| `POST /api/appusers` | Public registration |
| `POST /api/appusers/login` | HTTP Basic credentials required; Bearer tokens are rejected |
| `GET`, `PUT`, `DELETE /api/appusers` | Authenticated user with the `MEMBER` scope |
| All `/api/notes` endpoints | Authenticated user with the `MEMBER` scope |

New users receive `MEMBER`. Scope values in persistence, user DTOs, and JWT claims
use the enum name, such as `MEMBER`. Spring authorities use the corresponding
constant, such as `SecurityScope.MEMBER_AUTHORITY` (`SCOPE_MEMBER`). Both forms are
defined centrally in `SecurityScope`. `ADMIN` does not automatically grant `MEMBER`.

### Register, log in, and call the API

```sh
curl -i -X POST http://localhost:8080/api/appusers \
  -H 'Content-Type: application/json' \
  -d '{"username":"joao","password":"correct-horse-battery-staple"}'

curl -i -X POST http://localhost:8080/api/appusers/login \
  --user 'joao:correct-horse-battery-staple'
```

Login returns a JSON object containing `token`. Tokens expire after four hours.
Use the returned token to call protected endpoints:

```sh
curl http://localhost:8080/api/appusers \
  -H 'Authorization: Bearer <token>'

curl -i -X POST http://localhost:8080/api/notes \
  -H 'Authorization: Bearer <token>' \
  -H 'Content-Type: application/json' \
  -d '{"title":"Project checklist","content":"Review tests and documentation."}'
```

A valid Bearer token sent to `/api/appusers/login` receives 403. Missing or invalid
Basic credentials receive 401. Token issuance requires credentials on every login;
there is no refresh-token endpoint.

### JWT keys

The default configuration reads RSA keys from `certs/public.pem` and
`certs/private.pem`, relative to the working directory. The checked-in pair is for
local development and tests. Use a separate pair for each deployed environment.

To generate a new pair in a separate directory:

```sh
mkdir -p /path/to/environment-certs
openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 \
  -out /path/to/environment-certs/private.pem
openssl pkey -in /path/to/environment-certs/private.pem -pubout \
  -out /path/to/environment-certs/public.pem
```

Override the locations with `JWT_RSA_PUBLIC_KEY=file:/path/to/public.pem` and
`JWT_RSA_PRIVATE_KEY=file:/path/to/private.pem`.

## Error responses

Spring MVC errors and mapped domain exceptions use RFC 9457 Problem Details with
`application/problem+json`. Domain handlers choose the HTTP status and public
detail; Spring supplies the default title and request-path `instance`. Unexpected
exceptions are logged and receive a generic 500 Problem Detail.

Domain failures are logged at warning level with stack traces. Unexpected failures
are logged at error level. Built-in MVC exceptions use Spring's default logging.

Authentication and authorization errors retain Spring Security's default formats:
401 for missing/invalid credentials and 403 for insufficient access. Some responses
have a Spring Boot JSON error body; Bearer errors can have an empty body with error
information in the `WWW-Authenticate` header.

## Tests and build

```sh
./mvnw test
./mvnw clean verify
```

Tests include domain/application unit tests, MVC tests using the production
security configuration, PostgreSQL integration tests using Testcontainers, and
module-boundary verification. Docker must be available for the integration tests.

## Logs, traces, and metrics

Spring Boot Actuator is included. Only `/actuator/health` is exposed over HTTP by
the current configuration.

The Docker image starts the application with the OpenTelemetry Java agent from
`otel/opentelemetry-javaagent.jar`. Running through Maven does not automatically
attach that agent.

`compose.yaml` also defines a Grafana LGTM service under the `production` Compose
profile. To start the database and telemetry stack locally:

```sh
docker compose --profile production up -d
```

Grafana is available at <http://localhost:3000>. OTLP ports 4317 and 4318 are exposed.
To attach the agent to a local Maven run and export over OTLP/HTTP:

```sh
OTEL_EXPORTER_OTLP_ENDPOINT=http://localhost:4318 \
OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf \
./mvnw spring-boot:run \
  -Dspring-boot.run.jvmArguments='-javaagent:otel/opentelemetry-javaagent.jar'
```

For a containerized application, configure an OTLP endpoint reachable from that
container. Use the telemetry trace identifiers for correlating requests and logs.

## Docker image

Build the application and image:

```sh
./mvnw clean verify
docker build -t spring-boot-template:local .
```

The image runs as the `spring` user and includes the application JAR and telemetry
agent. Mount your environment's certificates at `/certs`, readable by that user.
Configure an accessible PostgreSQL datasource and Flyway credentials through the
application's environment variables. The Compose file runs supporting services;
it does not define an application container.

## GitHub automation

The workflow in `.github/workflows/ci-cd-workflow.yaml` runs for pushes to `main`
and pull requests targeting `main`. It has two jobs:

1. Run `mvn -B clean verify` on JDK 25 and upload the Dockerfile, application JAR,
   and telemetry agent as artifacts.
2. Download those artifacts, generate image metadata, and build the Docker image.
   The image-publishing `push` input is currently commented out.

No deployment job or dedicated vulnerability-scanning job is currently configured.
Repository branch protection and merge rules are managed in GitHub settings.

Dependabot checks Maven, Docker, and GitHub Actions dependencies weekly.
`.github/semantic.yml` configures Conventional Commit prefixes for PR titles,
including `feat`, `fix`, `docs`, `refactor`, `test`, `build`, `ci`, and `chore`.
