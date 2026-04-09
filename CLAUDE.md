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

Multi-module Maven project with three Spring Boot 3.2 microservices:

- **gateway** (port 4000) — Spring Cloud Gateway, routes `/uaa/**` to uaa-service and `/ft/**` to finance-tracker-service. Aggregates OpenAPI docs.
- **uaa-service** (port 8082) — User authentication & authorization. Manages users, roles, tokens (reset/verification), and email notifications. Uses Hazelcast for distributed caching and tracks login attempts.
- **finance-tracker-service** (port 8090) — Core business domain. Manages wallets, transactions (INCOME/EXPENSE), categories, and budgets scoped to users.

### Service Discovery & Config

Uses **Spring Cloud Kubernetes** (Fabric8) for service discovery and ConfigMap-based configuration. To run locally, disable Kubernetes in `bootstrap.yml`. Each service has `bootstrap.yml` for cloud config bootstrap.

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

- Java 21, Spring Boot 3.2.3, Spring Cloud 2023.0.0
- Moneta (Java Money) for currency handling in finance-tracker-service
- ShedLock for distributed job locking
- SpringDoc OpenAPI 2.2 for API documentation (`/v3/api-docs`, `/swagger-ui.html`)
- Micrometer + Prometheus for metrics

## Local Development

Docker Compose stack in `docker/docker-compose.yml` provides PostgreSQL, Keycloak, Prometheus, and Grafana. Kubernetes manifests in `.k8s/` directory.

Docker images are built via Fabric8 docker-maven-plugin during the `package` phase.