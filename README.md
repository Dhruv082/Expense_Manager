# Mini Expense Manager

A full-stack expense tracking system designed to simulate a **financial intelligence backend** rather than a simple CRUD application.

The application automatically categorizes expenses, detects anomalous spending behavior, and exposes analytical insights while maintaining a clean user workflow.

The focus of this project is **backend reasoning + user clarity**, meaning the system not only processes data but explains its decisions to the user.

---

## Key Features

### Expense Processing

* Manual expense entry
* CSV bulk upload ingestion
* Vendor identity resolution (case-insensitive learning)
* Automatic rule-based categorization (Swiggy → Food, Uber → Transport)

### Intelligent Behavior

* Anomaly detection (expense > 3× category average)
* Explainable anomaly reason returned in API response
* Duplicate-safe ingestion (idempotent CSV upload)
* Learned vendor knowledge base

### Analytics

* Monthly totals per category
* Top 5 vendors by total spend
* Category spend lookup endpoint
* Recent transactions endpoint
* Anomaly list & anomaly count

### Operational Endpoints

* Health check endpoint
* Quick inspection endpoints for verification without database access

---

## Technology Stack

**Backend**

* Java Spring Boot
* JPA / Hibernate
* PostgreSQL
* Apache Commons CSV

**Frontend**

* React + TypeScript
* Axios
* Lightweight CSS UI (focus on clarity, not decoration)

---

## Architecture Philosophy

This system is designed as a **processing pipeline**, not a data storage app.

Every expense follows the same lifecycle:

```
Input → Vendor Resolution → Categorization → Anomaly Evaluation → Persistence → Analytics
```

The application separates three layers of responsibility:

### 1. Knowledge Layer

Stores learned information about vendors and categories
(Example: once Swiggy is learned as Food, all future transactions use it)

### 2. Transaction Layer

Stores raw expense events (immutable financial records)

### 3. Intelligence Layer

Derives insights such as anomalies and analytics using historical behavior

This separation ensures:

* consistent analytics
* no duplicated business logic
* extendable rules engine

---

## Important Backend Decisions

### Idempotent Ingestion

CSV uploads and repeated API calls do not duplicate data.
The system detects previously inserted transactions using vendor + date + amount + description.

### Explainable Anomaly Detection

Instead of returning only `anomaly=true`, the API explains why:

```
1200 is greater than 3× category average (avg: 180.00)
```

This improves trust and usability.

### Database-Level Analytics

Aggregation queries (monthly totals) use native SQL because date extraction functions vary across dialects and are more reliable at the database level.

### Shared Processing Pipeline

Manual entry and CSV ingestion both pass through the same service logic to guarantee consistent behavior.

---

## Setup Instructions

### Backend

Codebase for BE is present in main branch

Create database:

```sql
CREATE DATABASE iexpense;
```

Update `application.properties`:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/iexpense
spring.datasource.username=postgres
spring.datasource.password=your_password
```

Run:

```
mvn spring-boot:run
```

Backend runs at:

```
http://localhost:8080
```

---

### Frontend

Codebase for FrontEnd is present in FE_ExpenseManager Branch

```
cd expense-ui
npm install
npm run dev
```

Frontend runs at:

```
http://localhost:5173
```

---

## CSV Format

```
date,amount,vendorName,description
01-02-2026,120,Swiggy,Lunch
02-02-2026,450,Uber,Airport ride
```

The parser safely ignores empty rows and incomplete entries.

---

## API Highlights

| Endpoint                                      | Purpose            |
| --------------------------------------------- | ------------------ |
| /expenses                                     | Create expense     |
| /expenses/upload                              | CSV ingestion      |
| /expenses/dashboard/monthly-totals            | Category analytics |
| /expenses/dashboard/top-vendors               | Vendor ranking     |
| /expenses/dashboard/category-total/{category} | Category lookup    |
| /expenses/recent                              | Quick verification |
| /expenses/health                              | Service status     |

---

## User Experience Considerations

* Immediate anomaly explanation after entry
* Safe re-upload of CSV files
* Simple dashboard readable without charts
* Minimal UI to reduce cognitive load
* Backend designed to be testable without database access

---

## Future Enhancements

* Editable categorization rules UI
* Machine learning based categorization
* Real-time anomaly notifications
* Recurring expense detection
* Budget limit alerts per category
* Visualization charts for trends
* Multi-user accounts & authentication
* Import from bank statements (PDF/Excel)

---

## Conclusion

This project focuses on building a small but thoughtfully designed financial backend system.
Instead of prioritizing visual complexity, the implementation emphasizes correctness, explainability, and maintainability — principles important for production backend engineering.
