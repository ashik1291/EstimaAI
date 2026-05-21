<h1 align="center">EstimaAI</h1>

<p align="center">
  <strong>AI-powered story point estimation for agile software teams</strong><br/>
  Eliminate estimation bias and speed up sprint planning using machine learning
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-ED8B00?style=flat&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=flat&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat&logo=mysql&logoColor=white"/>
  <img src="https://img.shields.io/badge/ML_Service-Python-3776AB?style=flat&logo=python&logoColor=white"/>
  <img src="https://img.shields.io/badge/Frontend-Flutter-02569B?style=flat&logo=flutter&logoColor=white"/>
  <img src="https://img.shields.io/badge/Integration-OpenFeign-6DB33F?style=flat&logo=spring&logoColor=white"/>
</p>

---

## What is EstimaAI?

EstimaAI is a full-stack intelligent estimation platform that helps agile teams **predict story points for user stories using machine learning**. Instead of relying on subjective team votes (planning poker), EstimaAI analyses the text of a user story and suggests an effort estimate — reducing bias and saving planning time.

The system is built as three independent services that communicate via REST:

```
Flutter App  ──►  Spring Boot Backend  ──►  ML Service (Python)
   (UI)              (Business Logic)         (NLP Prediction)
```

---

## Architecture

```
┌─────────────────┐     REST API     ┌──────────────────────┐     Feign Client     ┌───────────────────┐
│   Flutter App   │ ──────────────►  │  Spring Boot Backend │ ──────────────────►  │   ML Service      │
│  (Mobile/Web)   │                  │   Java 17 + MySQL    │                      │  Python + NLP     │
│                 │ ◄──────────────  │  Business Logic      │ ◄──────────────────  │  Story Prediction │
└─────────────────┘   JSON Response  └──────────────────────┘   Prediction Result  └───────────────────┘
```

**Backend responsibilities:**
- User and project management
- Story CRUD and sprint organisation
- Routing estimation requests to the ML service via OpenFeign
- Persisting prediction history per project

**ML Service responsibilities:**
- Receives story text as input
- Runs NLP-based classification model
- Returns predicted story point value (Fibonacci scale: 1, 2, 3, 5, 8, 13)

---

## Tech stack

| Layer | Technology |
|---|---|
| Backend language | Java 17 |
| Backend framework | Spring Boot 3.x |
| Service communication | OpenFeign (HTTP client) |
| Database | MySQL 8 |
| ORM | Spring Data JPA / Hibernate |
| ML service | Python (see ML repo) |
| Frontend | Flutter (see Frontend repo) |
| Build tool | Maven |

---

## Project repositories

This project is split across three repositories:

| Part | Repository | Description |
|---|---|---|
| Backend (this repo) | [ashik1291/EstimaAI](https://github.com/ashik1291/EstimaAI) | Spring Boot REST API and business logic |
| ML Service | [tfahim007/EstimaAI](https://github.com/tfahim007/EstimaAI) | Python NLP model and prediction API |
| Frontend | [Jahidul007/estima_ai_app](https://github.com/Jahidul007/estima_ai_app) | Flutter mobile/web app |

---

## Getting started

### Prerequisites

- Java 17+
- MySQL 8.0+
- ML Service running (see ML repo for setup)
- Maven 3.8+

### 1. Clone the backend

```bash
git clone https://github.com/ashik1291/EstimaAI.git
cd EstimaAI/estimaai
```

### 2. Set up the database

```sql
CREATE DATABASE estimaai;
```

### 3. Configure application

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/estimaai
    username: your_db_username
    password: your_db_password

ml:
  service:
    url: http://localhost:8000   # ML service host and port
```

### 4. Start the ML service first

Follow the setup instructions in [tfahim007/EstimaAI](https://github.com/tfahim007/EstimaAI) and ensure it is running before starting the backend.

### 5. Run the backend

```bash
mvn spring-boot:run
```

The backend API will start on `http://localhost:8080`.

### 6. Run the frontend

Follow the setup instructions in [Jahidul007/estima_ai_app](https://github.com/Jahidul007/estima_ai_app) to run the Flutter app and point it to your backend URL.

---

## API overview

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/projects` | Create a new project |
| `GET` | `/api/projects/{id}/stories` | List all user stories in a project |
| `POST` | `/api/stories` | Add a new user story |
| `POST` | `/api/stories/{id}/estimate` | Request ML-based story point estimate |
| `GET` | `/api/stories/{id}/history` | View estimation history for a story |

Test all endpoints using Postman. A Postman collection can be added to the repo on request.

---

## Key engineering decisions

- **OpenFeign** was chosen for ML service integration to keep HTTP communication declarative and easy to maintain, consistent with the Spring ecosystem
- **MySQL** over NoSQL — story and project data is relational and benefits from ACID transactions
- **Separated ML service** — keeps the Python model independently deployable and replaceable without touching the Java backend

---

## Contributors

| Role | Contributor |
|---|---|
| Backend (Spring Boot) | [ashik1291](https://github.com/ashik1291) |
| ML Service (Python/NLP) | [tfahim007](https://github.com/tfahim007) |
| Frontend (Flutter) | [Jahidul007](https://github.com/Jahidul007) |

---

## License

This project was developed as a collaborative engineering and AI research project. Feel free to fork and adapt.

---

<p align="center">
  Built with Java · Spring Boot · Python · Flutter
</p>
