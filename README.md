# Clients API
***
## Project Description
**Clients API** is a simple RESTful service built with **Spring Boot**. 
It provides client management with full **CRUD** (Create, Read, Update, Delete) operations. 
- The application also integrates authentication and authorization using a relational database.
- All API responses, including errors and exceptions, are returned in JSON format.
- Interactive documentation is available via Swagger UI.
***
## 🚀 Technologies
- Maven
- Java 24
- Spring Boot 3.5.5
- Spring Security (with `JdbcUserDetailsManager`)
- Spring Data JPA (Hibernate)
- H2 Database (file-based)
- Swagger / Springdoc OpenAPI 3

## 🗄 Database

The application uses an **H2 Database** in file mode:  
`jdbc:h2:file:./data/clients-api-db`

- H2 Console: http://localhost:8080/h2-console


JPA configuration:
- `spring.jpa.hibernate.ddl-auto=update` – automatically updates schema at startup
- `spring.sql.init.mode=always` – initializes schema and data from SQL files
- `spring.sql.init.platform=h2` – platform set to H2

Database schema and seed data are defined in:
- `src/main/resources/schema.sql` – schema definitions (`clients`, `app_users`, `app_authorities`)
- `src/main/resources/data.sql` – initial data 

## 📡 Endpoints

All resources are available under `/api/**`.  
Swagger UI is available at:
http://localhost:8080/docs


### CRUD Endpoints for Clients:

- `GET /api/clients` – get all clients
- `POST /api/clients` – create a new client
- `GET /api/clients/{id}` – get client details by ID
- `PUT /api/clients/{id}` – update client details
- `DELETE /api/clients/{id}` – delete a client

#### Example response (GET `/api/clients/1`):
```json
{
  "id": 1,
  "firstName": "Robert",
  "lastName": "Lewandowski",
  "email": "robert.lewandowski@fcbarcelona.com"
}
```
## 🔐 Security & Roles
Spring Security is integrated with a database using `JdbcUserDetailsManager`.
User data and roles are stored in `app_users and` `app_authorities` tables.
Passwords are encoded using `BCryptPasswordEncoder`.

Role-based access:
#### USER → read-only access

#### MANAGER → read, create, and update clients

#### ADMIN → full access, including delete operations

### Error Handling

Examples of returned HTTP status codes:

- 401 Unauthorized – user not authenticated

- 403 Forbidden – user lacks the required role

- 404 Not Found – client not found

- 400 Bad Request – validation errors

Example error response:
```json
{
"timestamp": "2025-08-23T14:58:52.2703786",
"status": 404,
"error": "Not Found",
"message": "Client with id 1 not found",
"path": "/api/clients/1",
"errors": null
}
```
## 🔑 Default Credentials (for testing)

The application initializes default users with roles from `data.sql`.  
You can use these accounts to log in and test role-based access control.

| Username | Password | Role    |
|----------|----------|---------|
| user     | user     | USER    |
| manager  | manager  | MANAGER |
| admin    | admin    | ADMIN   |

⚠️ These credentials are intended **only for local development and testing**.  
Do not use them in production environments.
## 🧪 Testing

The project includes integration tests for the REST API endpoints, verifying role-based access and CRUD operations:

#### USER role:
- Can read client data (GET /api/clients and GET /api/clients/{id})
- Cannot create, update, or delete clients (POST, PUT, DELETE)
#### MANAGER role:
- Can create and update clients (POST /api/clients and PUT /api/clients/{id})
- Cannot delete clients (DELETE)
#### ADMIN role:
- Full access: read, create, update, delete clients

All tests are implemented with Spring Boot Test, MockMvc, and `@WithMockUser` for authentication simulation. Each test runs within a transactional context, so any database changes are rolled back automatically, keeping the H2 database consistent after tests.

Example usage of tests:
```
./mvnw test
```

This will execute all integration tests, checking endpoints for correct responses and role-based access control.
***
## ▶️ Running the Application
Clone the repository:
```
git clone https://github.com/kcn3333/projects-java.git
cd clients-api
```
Start the application:

```
./mvnw spring-boot:run
```

or
```
mvn clean package
java -jar target/clients-api-0.0.1-SNAPSHOT.jar
```

Open Swagger UI:

http://localhost:8080/docs

## 🐳 Docker


You can also run the application inside a Docker container.
```
docker run -p 8080:8080 kcn333/clients-api:latest
```
or with docker-compose.yml:
```yaml
services:
  clients-api:
    image: kcn333/clients-api:1.0
    container_name: clients-api
    ports:
      - "8080:8080"
    volumes:
      - ./data:/app/data

```

### 📜 License

This project is available under the MIT License. You are free to use, modify, and distribute it according to the license terms.
