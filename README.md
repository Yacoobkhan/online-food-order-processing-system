# Online Food Order Processing System

## Overview

An event-driven, microservices-based food ordering system built with **Spring Boot**, **Camunda BPM**, **Apache ActiveMQ**, **MySQL**, and **React**. Users can place food orders and track them in real-time through a React dashboard that polls the backend every **2 seconds**. The order lifecycle is fully orchestrated by a Camunda BPMN workflow, from payment processing through kitchen preparation to delivery assignment.

---

## Tech Stack

| Technology | Purpose |
|------------|---------|
| Java 17 | Backend language |
| Spring Boot | Microservices framework |
| React + Vite | Frontend SPA |
| Camunda BPM | BPMN workflow orchestration |
| Apache ActiveMQ | Asynchronous messaging |
| MySQL | Relational database |
| Spring Data JPA | ORM / Persistence |
| Maven | Build tool |

---

## Features

- 🛒 Place food orders via a React UI
- 📊 Real-time order tracking with 2-second polling
- ⚙️ Camunda BPMN workflow orchestration
- 📨 ActiveMQ asynchronous messaging
- 💳 Payment success / failure handling
- 🍳 Kitchen processing simulation
- 🚚 Delivery assignment simulation

---

## Workflow

### Success Flow

```
PLACED → PAYMENT → KITCHEN → DELIVERY → DELIVERED
```

### Failure Flow

```
PLACED → PAYMENT → CANCELLED
```

---

## Project Structure

```
Food_Order_System/
├── order-service/       ← Port 8081 (Core Service + Camunda Engine)
├── payment-service/     ← Port 8082
├── kitchen-service/     ← Port 8083
├── delivery-service/    ← Port 8084
└── frontend/            ← Port 5173 (React + Vite)
```

---

## Configuration

Before running the project:

- Install and start **MySQL**.
- Create the following databases:
  - `orderdb`
  - `paymentdb`
  - `kitchendb`
  - `deliverydb`
- Update the MySQL username and password in each microservice's `application.properties` file according to your local MySQL installation.
- Download and start **Apache ActiveMQ** (default credentials: **admin/admin**).
- Download **Camunda Modeler** to view or edit the BPMN workflow.
- Camunda embedded admin credentials:
  - Username: **admin**
  - Password: **admin**

---

## How to Run

### Clone the repository

```bash
git clone https://github.com/Yacoobkhan/food-order-system.git
cd food-order-system
```

### Create MySQL Databases

```sql
CREATE DATABASE orderdb;
CREATE DATABASE paymentdb;
CREATE DATABASE kitchendb;
CREATE DATABASE deliverydb;
```

### Start Apache ActiveMQ

```bash
activemq start
```

### Run the Spring Boot Microservices (in separate terminals)

```bash
cd payment-service
./mvnw spring-boot:run
```

```bash
cd kitchen-service
./mvnw spring-boot:run
```

```bash
cd delivery-service
./mvnw spring-boot:run
```

```bash
cd order-service
./mvnw spring-boot:run
```

### Start the React Frontend

```bash
cd frontend
npm install
npm run dev
```

### Open in Browser

```
http://localhost:5173
```

---

## Author

**Yacoob Khan**

- GitHub: https://github.com/Yacoobkhan
- LinkedIn: https://linkedin.com/in/yacoobkhan23
- Email: yacoobkhan18@gmail.com
