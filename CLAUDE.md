# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
# Build all modules (skipping tests)
./mvnw clean install -DskipTests

# Build a single module
./mvnw clean install -pl finance-tracker-service -DskipTests

# Run unit tests (all modules)
./mvnw test

# Run unit tests for a single module
./mvnw test -pl finance-tracker-service

# Run a single test class
./mvnw test -pl finance-tracker-service -Dtest=WalletServiceImplTest

# Run integration tests (files matching *IT.java, uses maven-failsafe-plugin)
./mvnw verify -pl uaa-service

# Generate JaCoCo coverage report (excludes *Config*, *DTO*, *Mapper* classes)
./mvnw verify
```

## Architecture

Multi-module Maven project with three Spring Boot 4.0 microservices:

- **gateway** (port 4000) — Spring Cloud Gateway, routes `/uaa/**` to uaa-service and `/ft/**` to finance-tracker-service. Aggregates OpenAPI docs.
- **uaa-service** (port 8082) — User authentication & authorization. Manages users, roles, tokens (reset/verification), and email notifications. Uses Hazelcast for distributed caching and tracks login attempts.
- **finance-tracker-service** (port 8090) — Core business domain. Manages wallets, transactions (INCOME/EXPENSE), categories, and budgets scoped to users.

### Service Discovery & Config

Uses **Spring Cloud Kubernetes** (native client) for service discovery and ConfigMap-based configuration. Kubernetes config is in `application.yml` (bootstrap.yml has been removed). To run locally, disable Kubernetes in `application.yml`.

### Security

All services are **OAuth2 Resource Servers** validating JWTs issued by **Keycloak**. Key classes:
- `KeycloakRealmRoleConverter` — converts Keycloak `realm_access.roles` to Spring authorities
- `UsernameSubClaimAdapter` — extracts `preferred_username` from JWT claims
- `SecurityConfig` — stateless sessions, CORS, method-level security via `@EnableMethodSecurity`

### Code Patterns

- **Layered architecture**: REST controllers (named `*Resource`) → Service interfaces → `*ServiceImpl` → Spring Data JPA repositories
- **DTO separation**: Request/response DTOs in `rest/dto/` packages, mapped via **MapStruct** mappers
- **Lombok** throughout: `@Data`, `@RequiredArgsConstructor`, `@Slf4j`, `@Builder`
- **Annotation processors** require specific order: spring-boot-configuration-processor, mapstruct-processor, lombok, lombok-mapstruct-binding
- **Global exception handling**: `@ControllerAdvice` classes (`GlobalExceptionHandler`) with custom exceptions (`EntityNotFoundException`, `EntityAlreadyExistException`)
- **Async**: Configured thread pools via `AsyncConfig` with custom exception handlers
- **Auditing** (uaa-service): `AbstractAuditingEntity` base class with `SpringSecurityAuditorAware`
- **Password validation** (uaa-service): Custom `@ValidPassword` constraint using Passay library

### Database

**PostgreSQL** with **Liquibase** migrations (no Hibernate auto-DDL). Migration changelogs at `src/main/resources/db/changelog/master.xml`. Initial schema and seed data loaded via CSV files.

- finance-tracker-service DB: `finance_tracker`
- uaa-service DB: `idp`

### Key Dependencies

- Java 25, Spring Boot 4.0.5, Spring Cloud 2025.1.0
- Moneta (Java Money) for currency handling in finance-tracker-service
- ShedLock for distributed job locking
- SpringDoc OpenAPI 3.0 for API documentation (`/v3/api-docs`, `/swagger-ui.html`)
- Micrometer + Prometheus for metrics

## CI/CD

GitHub Actions workflow in `.github/workflows/ci.yml`:

- **Change detection**: `dorny/paths-filter` identifies which services changed (root `pom.xml` changes trigger all)
- **Build & Test**: Full Maven verify + JaCoCo coverage on every push/PR to master/develop
- **Docker build/push**: Only changed services, only on push to master. Images pushed to Docker Hub as `sevenup3/<service>:<commit-sha>` and `sevenup3/<service>:latest`
- **Deploy**: Helm upgrade per changed service to k3d-prod via self-hosted runner

## Infrastructure & Deployment

```
infra/
  helm/                    # Helm charts (one per service + infrastructure)
    gateway/
    uaa-service/
    finance-tracker-service/
    infrastructure/        # PostgreSQL, Keycloak, RBAC, Ingress, Config Watcher
  docker/                  # Docker Compose for local development
    docker-compose.yml
```

- **Helm charts**: Each service is an independent Helm release. Deploy with `helm upgrade --install <service> ./infra/helm/<service> -n prod`
- **Infrastructure**: Deployed manually via `helm upgrade --install infrastructure ./infra/helm/infrastructure -n prod`
- **Legacy manifests**: Raw K8s YAML preserved in `.k8s-legacy/` for reference
- **Docker images**: Multi-stage Dockerfiles per module using `eclipse-temurin:25-jre-alpine` with Spring Boot layer extraction

## Local Development

Docker Compose stack in `infra/docker/docker-compose.yml` provides PostgreSQL, Keycloak, Prometheus, and Grafana.