# FinanceTracker CI/CD Plan

## Overview

Multi-phase CI/CD strategy for the FinanceTracker multi-module Maven monorepo, deploying 3 Spring Boot microservices to Kubernetes (k3d locally, future migration to GKE/EKS).

**Docker Hub registry:** `sevenup3/<service-name>`
**Services:** gateway, uaa-service, finance-tracker-service

---

## Phase 1: CI Pipeline (`.github/workflows/ci.yml`) — IMPLEMENTED

### Architecture

```
Push/PR to master/develop
        |
   detect-changes ────────────────────────────────
        |                                          |
   build-and-test                            outputs: which
   (full Maven verify + JaCoCo)              services changed
        |                                          |
   docker-build-and-push ←── matrix: only changed services
   (parallel, master push only)
```

### Key Design Decisions

1. **Selective Docker builds** — Uses `dorny/paths-filter` to detect which modules changed. Only builds/pushes Docker images for affected services. Root `pom.xml` changes trigger all services.

2. **Full Maven verify always runs** — Even if only one service changed, the full build runs because shared parent POM or transitive dependencies could break other modules.

3. **Docker push only on master** — PRs and develop pushes run build+test only. Docker images are built and pushed only on push to master.

4. **Dynamic matrix** — The `docker-build-and-push` job uses `fromJson()` to create parallel build jobs only for changed services.

5. **JaCoCo coverage reporting** — Per-module coverage summary displayed in CI output. No enforcement thresholds yet (low test coverage baseline).

### Triggers

| Event | build-and-test | docker-build-and-push |
|-------|:-:|:-:|
| PR to master/develop | YES | NO |
| Push to develop | YES | NO |
| Push to master | YES | YES (changed services only) |

### Docker Image Tags

- `sevenup3/<service>:<commit-sha>` — immutable, used for deployments
- `sevenup3/<service>:latest` — mutable, convenience

### GitHub Secrets Required

| Secret | Description |
|--------|-------------|
| `DOCKER_HUB_USERNAME` | Docker Hub username |
| `DOCKER_HUB_TOKEN` | Docker Hub access token |

---

## Phase 2: CD with Helm (`deploy-prod` job in `ci.yml`) — IMPLEMENTED

### Architecture

```
Push to master
        |
   detect-changes → build-and-test → docker-build-and-push → deploy-prod
                                      (changed services)       (helm upgrade
                                                                per changed service,
                                                                self-hosted runner,
                                                                k3d-prod cluster)
```

### Helm Chart Structure

Separate chart per service + shared infrastructure chart:

```
charts/
  gateway/               # Independent Helm release
  uaa-service/           # Independent Helm release
  finance-tracker-service/  # Independent Helm release + ConfigMap
  infrastructure/        # PostgreSQL, Keycloak, RBAC, Ingress (manual deploy)
```

### Key Design Decisions

1. **Separate charts per service** — Each service is an independent Helm release. Aligns with selective deploy strategy — only changed services get `helm upgrade`.

2. **Single `prod` namespace** — Resource-constrained local k3d. Dev/staging can be added later via `values-<env>.yaml` files.

3. **Infrastructure deployed manually** — Stateful components (PostgreSQL, Keycloak) are not part of the automated pipeline. Deploy with: `helm upgrade --install infrastructure ./charts/infrastructure -n prod`

4. **Deploy uses same dynamic matrix as Docker build** — The `deploy-prod` job reuses the `detect-changes` outputs, keeping CI and CD perfectly aligned.

### Deploy Command (per service)

```bash
helm upgrade --install <service> ./charts/<service> \
  --set image.tag=<commit-sha> \
  --namespace prod --create-namespace --wait --timeout 5m
```

### Legacy Manifests

Original raw K8s manifests preserved in `.k8s-legacy/` for reference.

---

## Phase 3: Production Hardening (PLANNED)

- **Tag-based releases** — `v*.*.*` tags for versioned production deployments
- **Rollback workflow** — `workflow_dispatch` with Helm rollback
- **Health checks + smoke tests** — Post-deploy validation

---

## Phase 4: Observability Stack (PLANNED)

- Deploy monitoring stack (Prometheus, Grafana) via `workflow_dispatch`
- Similar to ops-tracker's `deploy-monitoring.yml`

---

## Phase 5: Security (PLANNED)

- CodeQL analysis workflow for Java/Kotlin
- Similar to ops-tracker's `code-ql.yml`
