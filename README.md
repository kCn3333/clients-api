# clients-api

> Spring Boot REST API for client management — deployed on Kubernetes via Helm chart and Flux GitOps.

---

## Overview

RESTful service providing full CRUD operations for client management with role-based access control. Built for production deployment on Kubernetes with full observability.

---

## Technologies

| Component | Technology |
| :--- | :--- |
| Language | Java 24 |
| Framework | Spring Boot 3.5.5 |
| Security | Spring Security (JdbcUserDetailsManager) |
| Database | PostgreSQL 17 (CloudNativePG) |
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
# Run with H2 (dev profile)
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

## Kubernetes Deployment

The application is deployed via Helm chart managed by Flux GitOps.

### Helm Chart

Located in `helm/clients-api/`. Chart version follows semantic versioning independently of the application version.

```
helm/clients-api/
├── Chart.yaml          # chart metadata and versions
├── values.yaml         # default configuration values
└── templates/
    ├── deployment.yaml
    ├── service.yaml
    ├── ingress.yaml
    ├── hpa.yaml               # min:2 max:6 replicas, CPU 70%
    ├── pdb.yaml               # minAvailable: 1
    ├── networkpolicy.yaml     # ingress from kube-system + monitoring only
    └── servicemonitor.yaml    # Prometheus scraping
```

### Values

Key configuration in `values.yaml`:

```yaml
image:
  repository: kcn333/clients-api
  tag: ""               # controlled by Flux ImagePolicy

springProfile: prod

database:
  host: clients-db-rw
  port: 5432
  name: clients_db
  credentialsSecret: clients-db-secret

ingress:
  host: clients-api.cluster.kcn333.com

autoscaling:
  enabled: true
  minReplicas: 2
  maxReplicas: 6
  targetCPUUtilizationPercentage: 70
```

### Flux Integration

Flux automatically deploys new versions when a semver tag is pushed:

```
git push origin v1.5.0
        │
        ▼
GitHub Actions: test → build → push to DockerHub
        │
        ▼
Flux ImagePolicy detects new tag
        │
        ▼
Flux commits updated tag to k3s-homelab repo
        │
        ▼
Flux HelmRelease upgrade triggered
```

---

## CI/CD Pipeline

GitHub Actions workflow on `v*` tags:

1. Run integration tests
2. Build Docker image
3. Push to DockerHub with tags: `1.x.x`, `1.x`, `sha-XXXX`, `latest`

---

## Observability

- **Metrics:** exposed at `/actuator/prometheus`, scraped by Prometheus via ServiceMonitor
- **Health:** `/actuator/health/liveness` and `/actuator/health/readiness`
- **Logs:** JSON structured logs, collected by Promtail → Loki
- **Grafana dashboard:** HTTP request rate, error rate, p99 latency, JVM heap, HikariCP connections

---

## License

MIT License.