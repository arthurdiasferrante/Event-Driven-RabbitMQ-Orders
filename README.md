# Pizzaria Service (Portfolio)

REST API on **Spring Boot** + **PostgreSQL**. **New orders go through RabbitMQ**: the HTTP handler only publishes a message; a listener consumes the queue and writes the order to the database.

`RabbitMQConfig` declares queue **`order.v1.order-created`**, direct exchange **`order.exchange`**, binding with routing key **`order.created.routingKey`**, and **`Jackson2JsonMessageConverter`** for JSON payloads.

- **`OrderService.createOrder`** → `AmqpTemplate.convertAndSend("order.exchange", "order.created.routingKey", orderRequestDTO)`.
- **`OrderService.processOrder`** → `@RabbitListener(queues = "order.v1.order-created")` loads client/pizzas, validates IDs, saves the **`Order`**.

So **RabbitMQ must be up** for `POST /orders` to eventually result in a persisted order (and for integration tests that hit the real stack). Other order operations (`GET`/`PUT`/`DELETE`) still talk to PostgreSQL through the service as usual.

## Features (High Level)

- Orders over REST + async create path via AMQP
- PostgreSQL via Spring Data JPA
- `spring.jpa.hibernate.ddl-auto=update` in dev

**What’s there on REST right now:**

- **Controllers → services → repositories** for the main flows.
- **DTOs** as Java `record`s under `dto/client`, `dto/pizza`, `dto/order`.
- **Clients** (`/clients`): CRUD; `ClientRequestDTO` / `ClientResponseDTO` (`name`, `address`, `id` on responses).
- **Pizzas** (`/pizzas`): CRUD + **get by id**; `name`, `ingredients`, `imageUrl`.
- **Orders** (`/orders`):
  - **`POST`**: publishes **`OrderRequestDTO` (`clientId`, `pizzaIds`)** to Rabbit; response **`202 Accepted`** with body **`Pedido enviado para a cozinha!`** (does not return the created entity synchronously).
  - **`GET`/`PUT`/`DELETE`**: `OrderResponseDTO` with order `id`, **client name**, **pizza names**, **`OrderStatus`**.
- **`OrderStatus`**: `PENDING`, `PREPARING`, `OUT_FOR_DELIVERY`, `ARRIVED` (defaults on new orders from the listener path).
- **`GlobalExceptionHandler`**: `OrderNotFoundException`, `ClientNotFoundException`, `PizzaNotFoundException` → HTTP **404**.
- **`OrderService`**: `markAsPreparing`, `dispatchForDelivery`, `orderDelivered` exist for status transitions—**not wired to controllers yet**.

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

- Code under **`pizzaria/`** (artifact **`pizzariarubens`**, package **`com.pizzaria`**).
- Entry point: `com.pizzaria.Main`.
- Rabbit: `com.pizzaria.config.RabbitMQConfig`.

## Architecture Overview

1. **`POST /orders`** enqueues an **`OrderRequestDTO`** on **`order.exchange`** with routing key **`order.created.routingKey`**.
2. The listener on **`order.v1.order-created`** validates client/pizza IDs and persists **`Order`** + relations in PostgreSQL.
3. **`GET`/`PUT`/`DELETE`** on orders (and all client/pizza endpoints) hit the app and DB directly—no queue involved for those.

## Requirements

- Java 21
- `./mvnw` (Maven Wrapper)
- PostgreSQL (local or reachable)
- **RabbitMQ** (local or reachable) — **required** for the create-order pipeline to land rows in the DB

## Configuration

Database in `src/main/resources/application.properties`:

- `spring.datasource.url=jdbc:postgresql://localhost:5432/pizzaria_db`
- `spring.datasource.username=${DB_USER}`
- `spring.datasource.password=${DB_PASSWORD}`

Set **`DB_USER`** and **`DB_PASSWORD`**.

Tune **`spring.rabbitmq.*`** in the same file if the broker isn’t on localhost with default guest credentials.

Full path from clone root: `pizzaria/src/main/resources/application.properties`. Dev enables `spring.jpa.show-sql=true` and the Postgres dialect.

## How to Run

1. PostgreSQL up; database **`pizzaria_db`** exists.
2. **RabbitMQ running** (queues/exchanges/bindings are created from `RabbitMQConfig` at startup).
3. Shell:

   ```bash
   export DB_USER="your_user"
   export DB_PASSWORD="your_password"
   ```

4. From **`pizzaria/`**:

   ```bash
   ./mvnw spring-boot:run
   ```

   From **repo root**:

   ```bash
   cd pizzaria
   ./mvnw spring-boot:run
   ```

5. API: `http://localhost:8080`.

## API routes (summary)

| Method | Path | Description |
|--------|------|-------------|
| GET, POST | `/clients` | List / create clients |
| GET, PUT, DELETE | `/clients/{id}` | Get, update, or delete a client |
| GET, POST | `/pizzas` | List / create pizzas |
| GET, PUT, DELETE | `/pizzas/{id}` | Get, update, or delete a pizza |
| GET, POST | `/orders` | List all orders / enqueue new order (`202` + message body) |
| GET, PUT, DELETE | `/orders/{id}` | Get one / update pizzas / delete |

JSON shapes: `dto/*RequestDTO` and `*ResponseDTO`.

## Testing

```bash
./mvnw test
```

From repo root: `cd pizzaria` then `./mvnw test`.

**Unit tests (Mockito)** live under `src/test/java/com/pizzaria/service/`, e.g. **`OrderServiceTest`** (publish + `processOrder` behavior) and **`ClientServiceTest`**.

## Notes 

- HTTP endpoints for **`markAsPreparing` / `dispatchForDelivery` / `orderDelivered`** are still optional follow-ups.
- If **`POST /orders`** returns **202** but nothing appears in the DB, check the broker, bindings, and listener logs—the write happens in **`processOrder`**, not in the controller.
