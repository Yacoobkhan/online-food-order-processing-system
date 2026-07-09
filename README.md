# 🍕 Online Food Order Processing System

## Overview

An event-driven, microservices-based food ordering system built with Spring Boot, Camunda BPMN, Apache ActiveMQ, MySQL, and React. Users can place food orders and track them in real-time through a React dashboard that polls the backend every 2 seconds. The order lifecycle is fully orchestrated by a Camunda BPMN workflow, from payment processing through kitchen preparation to delivery assignment.

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Backend language |
| Spring Boot | Microservices framework |
| React | Frontend SPA |
| Camunda BPM | BPMN workflow orchestration |
| Apache ActiveMQ | Asynchronous messaging |
| MySQL | Relational database |
| Spring Data JPA | ORM / persistence |
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

**Success Flow:**
```
PLACED → PAYMENT → KITCHEN → DELIVERY → DELIVERED
```

**Failure Flow:**
```
PLACED → PAYMENT → CANCELLED
```

---

## Project Structure

```
Food_Order_System/
├── order-service/       ← Port 8081 (Core hub + Camunda engine)
├── payment-service/     ← Port 8082
├── kitchen-service/     ← Port 8083
├── delivery-service/    ← Port 8084
└── frontend/            ← Port 5173 (React + Vite)
```

---

## How to Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/Yacoobkhan/food-order-system.git
   cd food-order-system
   ```

2. **Start MySQL** and create the database:
   ```sql
   CREATE DATABASE food_order_db;
   ```

3. **Start Apache ActiveMQ**
   ```bash
   activemq start
   ```

4. **Run the Spring Boot microservices** (in separate terminals):
   ```bash
   cd payment-service  && ./mvnw spring-boot:run
   cd kitchen-service  && ./mvnw spring-boot:run
   cd delivery-service && ./mvnw spring-boot:run
   cd order-service    && ./mvnw spring-boot:run
   ```

5. **Start the React frontend**
   ```bash
   cd frontend
   npm install
   npm run dev
   ```

6. **Open in browser:** [http://localhost:5173](http://localhost:5173)

---

## Author

**Yacoob Khan**

[![GitHub](https://img.shields.io/badge/GitHub-Yacoobkhan-181717?style=flat-square&logo=github)](https://github.com/Yacoobkhan)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-yacoobkhan23-0A66C2?style=flat-square&logo=linkedin)](https://linkedin.com/in/yacoobkhan23)
[![Email](https://img.shields.io/badge/Email-yacoobkhan18@gmail.com-EA4335?style=flat-square&logo=gmail)](mailto:yacoobkhan18@gmail.com)
