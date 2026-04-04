# Pizzaria Service (Portfolio)

REST API for orders on **Spring Boot** + **PostgreSQL**, with **RabbitMQ** in the picture for async work once the queue side is finished.

`spring-boot-starter-amqp` is already in the POM; producers/consumers and broker config are still coming. The numbered flow under *Architecture* is where things are headed, not necessarily everything that runs today.

## Features (High Level)

- Orders over REST
- PostgreSQL via Spring Data JPA
- RabbitMQ for async workflows (same idea as below—still being hooked up)
- `spring.jpa.hibernate.ddl-auto=update` in dev

**What’s there on REST right now:**

- **Clients** (`/clients`): CRUD
- **Pizzas** (`/pizzas`): create/list/update/delete; ingredients list + optional `imageUrl`
- **Orders** (`/orders`): CRUD; order has one `Client` and many `Pizzas` (`ManyToMany`, join table `order_pizza`)
- `GlobalExceptionHandler` + `OrderNotFoundException` for missing orders

## Tech Stack

- Java 21
- Spring Boot 3.2.x (`pom.xml` has **3.2.4**)
  - `spring-boot-starter-web`
  - `spring-boot-starter-data-jpa`
  - `spring-boot-starter-amqp`
- PostgreSQL
- Lombok
- Maven (`mvnw`)

## Project layout

- Code lives under **`pizzaria/`** (artifact **`pizzariarubens`**, package **`com.pizzaria`**).
- Entry point: `com.pizzaria.Main`.

## Architecture Overview

1. HTTP hits the API to create or change orders (and related data).
2. Data goes to PostgreSQL.
3. Important events (e.g. `OrderCreated`) go to RabbitMQ.
4. Workers consume those messages (validation, fulfillment, notifications, whatever)—so slow work doesn’t block the HTTP thread.

## Requirements

- Java 21
- `./mvnw` (Maven Wrapper)
- PostgreSQL (local or reachable)
- RabbitMQ (local or reachable) when you start using the broker

## Configuration

Database bits in `src/main/resources/application.properties`:

- `spring.datasource.url=jdbc:postgresql://localhost:5432/pizzaria_db`
- `spring.datasource.username=${DB_USER}`
- `spring.datasource.password=${DB_PASSWORD}`

Need:

- `DB_USER`
- `DB_PASSWORD`

Add RabbitMQ settings in the same file when you wire it up; local defaults are fine for tinkering.

Full path from clone root: `pizzaria/src/main/resources/application.properties`. Dev also turns on `spring.jpa.show-sql=true` and the Postgres dialect.

## How to Run

1. PostgreSQL up; database **`pizzaria_db`** exists.
2. RabbitMQ running if you’re exercising queues (optional until then).
3. Shell:

   ```bash
   export DB_USER="your_user"
   export DB_PASSWORD="your_password"
   ```

4. Run (from **`pizzaria/`**):

   ```bash
   ./mvnw spring-boot:run
   ```

   From the **repo root**:

   ```bash
   cd pizzaria
   ./mvnw spring-boot:run
   ```

5. API on `http://localhost:8080` (default Spring Boot port).

## API routes (summary)

| Method | Path | Description |
|--------|------|-------------|
| GET, POST | `/clients` | List / create clients |
| GET, PUT, DELETE | `/clients/{id}` | Get, update, or delete a client |
| GET, POST | `/pizzas` | List / create pizzas |
| PUT, DELETE | `/pizzas/{id}` | Update or delete a pizza |
| GET, POST | `/orders` | List / create orders |
| GET, PUT, DELETE | `/orders/{id}` | Get, update, or delete an order |

JSON matches the `Client`, `Pizza`, and `Order` entities.

## Testing

```bash
./mvnw test
```

Repo root:

```bash
cd pizzaria
./mvnw test
```

## Notes 

- Room for more order-domain code and real RabbitMQ producers/consumers.
- Idea: keep HTTP thin; shove slow or noisy work to the queue when it exists.
- Clients/pizzas/orders endpoints are in; queue code is the next chunk.
