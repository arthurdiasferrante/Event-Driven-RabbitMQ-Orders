# Pizzaria Service (Portfolio)

REST API for orders on **Spring Boot** + **PostgreSQL**, with **RabbitMQ** config started (queues/exchange/binding) for when you wire publishers and consumers.

`RabbitMQConfig` already declares a queue (`order.v1.order-created`), a direct exchange (`meu retorno`), a binding with routing key `order.created.routingKey`, and a `Jackson2JsonMessageConverter`. Nothing sends or listens yet—no `RabbitTemplate` usage or `@RabbitListener` in the tree.

## Features (High Level)

- Orders over REST
- PostgreSQL via Spring Data JPA
- RabbitMQ pieces in code (see above); full async flow still TBD
- `spring.jpa.hibernate.ddl-auto=update` in dev

**What’s there on REST right now:**

- **Controllers → services → repositories** (no fat controllers talking straight to the DB for the main flows).
- **DTOs** as Java `record`s under `dto/client`, `dto/pizza`, `dto/order`—HTTP payloads are not raw JPA entities.
- **Clients** (`/clients`): CRUD; body fields `name`, `address`; responses include `id`.
- **Pizzas** (`/pizzas`): create, list, **get by id**, update, delete; `name`, `ingredients`, `imageUrl`.
- **Orders** (`/orders`): CRUD; body uses **`clientId`** + **`pizzaIds`** (and the `OrderRequestDTO` record also carries `orderStatus`, though create/update paths don’t apply it yet—new orders default to **`PENDING`** on the entity). Responses return **`OrderResponseDTO`**: order `id`, **client name**, **pizza names**, and **`OrderStatus`**.
- **`OrderStatus`** enum on orders: `PENDING`, `PREPARING`, `OUT_FOR_DELIVERY`, `ARRIVED`.
- **`GlobalExceptionHandler`** maps **`OrderNotFoundException`**, **`ClientNotFoundException`**, and **`PizzaNotFoundException`** to HTTP 404.
- **`OrderService`** has **`markAsPreparing`**, **`dispatchForDelivery`**, and **`orderDelivered`** for status transitions—**not exposed on controllers yet** (call from code/tests or add endpoints when you want).

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
- Config example: `com.pizzaria.config.RabbitMQConfig`.

## Architecture Overview

1. HTTP hits the API to create or change orders (and related data).
2. Data goes to PostgreSQL.
3. Important events (e.g. `OrderCreated`) are meant to go to RabbitMQ once publishing exists.
4. Workers would consume those messages (validation, fulfillment, notifications, whatever)—so slow work doesn’t block the HTTP thread.

## Requirements

- Java 21
- `./mvnw` (Maven Wrapper)
- PostgreSQL (local or reachable)
- RabbitMQ (local or reachable) if you want the broker up; optional until you use it

## Configuration

Database bits in `src/main/resources/application.properties`:

- `spring.datasource.url=jdbc:postgresql://localhost:5432/pizzaria_db`
- `spring.datasource.username=${DB_USER}`
- `spring.datasource.password=${DB_PASSWORD}`

Need:

- `DB_USER`
- `DB_PASSWORD`

Add RabbitMQ host/user/password in the same file when you go beyond defaults; Spring Boot AMQP will pick up `spring.rabbitmq.*` when you set them.

Full path from clone root: `pizzaria/src/main/resources/application.properties`. Dev also turns on `spring.jpa.show-sql=true` and the Postgres dialect.

## How to Run

1. PostgreSQL up; database **`pizzaria_db`** exists.
2. RabbitMQ running if you’re exercising the broker (optional until then).
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
| GET, PUT, DELETE | `/pizzas/{id}` | Get, update, or delete a pizza |
| GET, POST | `/orders` | List / create orders |
| GET, PUT, DELETE | `/orders/{id}` | Get, update, or delete an order |

JSON shapes follow the `*RequestDTO` / `*ResponseDTO` records (see `dto/`).

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

- Room for RabbitMQ producers/consumers and HTTP endpoints for order status transitions if you want them public.
- Idea: keep HTTP thin; push slow or noisy work to the queue when publishing exists.
- Clients/pizzas/orders are on REST with DTOs + services; queue wiring is the next chunk.
