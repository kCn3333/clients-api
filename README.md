# clients-api

> Spring Boot REST API for client management — deployed on Kubernetes via Helm chart and Flux GitOps with progressive delivery.

---

## Overview

RESTful service providing full CRUD operations for client management with role-based access control. Built for production deployment on Kubernetes with full observability and progressive delivery pipeline.

---

## Technologies

| Component | Technology |
| :--- | :--- |
| Language | Java 24 |
| Framework | Spring Boot 3.5.5 |
| Security | Spring Security (JdbcUserDetailsManager) |
| Database | PostgreSQL 17 (CloudNativePG) / H2 (dev) |
| ORM | Spring Data JPA (Hibernate) |
| Docs | Springdoc OpenAPI 3 (Swagger UI) |
| Metrics | Micrometer + Prometheus |
| Build | Maven |
| Container | Docker |

---

## API Endpoints

All resources available under `/api/**`. Swagger UI at `/docs`.

| Method | Path | Role | Description |
| :--- | :--- | :--- | :--- |
| GET | `/api/clients` | USER | Get all clients |
| GET | `/api/clients/{id}` | USER | Get client by ID |
| POST | `/api/clients` | MANAGER | Create client |
| PUT | `/api/clients/{id}` | MANAGER | Update client |
| DELETE | `/api/clients/{id}` | ADMIN | Delete client |

### Example response

```json
{
  "id": 1,
  "firstName": "Robert",
  "lastName": "Lewandowski",
  "email": "robert.lewandowski@fcbarcelona.com"
}
```

---

## Security & Roles

| Username | Password | Role |
| :--- | :--- | :--- |
| user | user | USER — read only |
| manager | manager | MANAGER — read + create + update |
| admin | admin | ADMIN — full access |

⚠️ Default credentials for testing only. Do not use in production.

---

## Local Development

```bash
# Run with H2 (local profile)
./mvnw spring-boot:run

# Run tests
./mvnw test

# Build JAR
./mvnw clean package

# Run with Docker
docker run -p 8080:8080 kcn333/clients-api:latest
```

Swagger UI: `http://localhost:8080/docs`

---

## CI/CD Pipeline

GitHub Actions workflow (`.github/workflows/ci.yml`) triggered on:
- **Pull requests to main** — runs tests only
- **Push to main** — tests + build + push Docker image (tag: `latest`, `sha-XXXX`)
- **Push tag `v*`** — tests + build + push Docker image + **publish Helm chart to GHCR**

### Jobs

```
test → build-and-push → publish-chart (tags only)
```

**publish-chart** automatically:
1. Sets `version` and `appVersion` in `Chart.yaml` from the git tag
2. Packages the Helm chart
3. Pushes to `oci://ghcr.io/kcn3333/charts/clients-api:X.Y.Z`

---

## Progressive Delivery

New versions flow through three environments before reaching production:

```
git tag v1.5.4
        │
        ▼
GitHub Actions builds and publishes chart + Docker image
        │
        ▼
Flux detects new tag via ImagePolicy
        │
        ├──→ dev (H2, auto-deploy)       clients-api-dev.cluster.kcn333.com
        │
        ├──→ staging (PostgreSQL, auto)  clients-api-staging.cluster.kcn333.com
        │
        └──→ prod (PostgreSQL, PR only)  clients-api.cluster.kcn333.com
```

### Promoting to production

```bash
# 1. Verify staging is healthy
curl -s -u user:user https://clients-api-staging.cluster.kcn333.com/api/clients

# 2. Create release branch in k3s-homelab repo
git checkout -b release/1.5.4
sed -i 's/tag: "old"/tag: "1.5.4"/' apps/base/clients-api/helmrelease.yaml
git commit -m "chore(release): promote clients-api 1.5.4 to production"
git push origin release/1.5.4

# 3. Open PR → review → merge → Flux deploys to prod
```

---

## Helm Chart

Located in `helm/clients-api/`. Published to GHCR as OCI image on every version tag.

```
helm/clients-api/
├── Chart.yaml             # placeholder — CI sets version/appVersion
├── values.yaml            # production defaults
├── values-staging.yaml    # staging overrides reference
└── templates/
    ├── deployment.yaml    # conditional DB env vars (skip for H2 profile)
    ├── service.yaml
    ├── ingress.yaml
    ├── hpa.yaml           # min:2 max:6, CPU 70% (prod only)
    ├── pdb.yaml           # minAvailable: 1 (prod only)
    ├── networkpolicy.yaml # intra-namespace + kube-system + monitoring
    ├── servicemonitor.yaml
    └── tests/
        └── test-connection.yaml  # helm test: health + API endpoint
```

### Key values

```yaml
image:
  repository: kcn333/clients-api
  tag: ""               # set by Flux ImagePolicy

springProfile: prod     # local / prod

database:
  host: clients-db-rw
  port: 5432
  name: clients_db
  credentialsSecret: clients-db-secret  # empty = H2 mode (no secret)

ingress:
  host: clients-api.cluster.kcn333.com

autoscaling:
  enabled: true
  minReplicas: 2
  maxReplicas: 6
  targetCPUUtilizationPercentage: 70
```

### Running helm test

```bash
helm test clients-api -n clients --logs
```

---

## Observability

- **Metrics:** `/actuator/prometheus` — scraped by Prometheus via ServiceMonitor
- **Health:** `/actuator/health/liveness` and `/actuator/health/readiness`
- **Logs:** JSON structured logs → Promtail → Loki
- **Grafana dashboard:** HTTP rate, error rate, p99 latency, JVM heap, HikariCP

### Custom alerts (PrometheusRule)

- `ClientsApiHighErrorRate` — >1% 5xx errors for 5 minutes
- `ClientsApiHighLatency` — p99 >2000ms for 5 minutes
- `ClientsApiPodRestarting` — >3 restarts in 1 hour

---

## License

MIT License.
