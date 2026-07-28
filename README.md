# TrackHire

🔗 **Live API:** https://trackhire-production-770a.up.railway.app
_Hosted on a free tier — the first request after a period of inactivity may take 30-60s to wake up._

A backend API for tracking job applications through their full lifecycle — from a pasted job description to offer. Built to solve a real problem: keeping dozens of job applications organized during an active job search, instead of scattered across emails and spreadsheets.

## What it does

- Paste a raw job description, and TrackHire uses Groq's LLM API to automatically extract the company, role, skills, location, and experience required — no manual data entry
- Review and confirm the extracted details before saving (nothing is auto-saved without confirmation)
- Track each application through a status pipeline: `SAVED → APPLIED → ASSESSMENT → INTERVIEW → OFFERED / REJECTED`
- Search applications by company name, filter by status
- Every user's data is private and scoped to their own account via JWT authentication

## Tech stack

- **Java 17, Spring Boot** — REST API
- **Spring Data JPA / Hibernate, MySQL** — persistence
- **Spring Security, JWT (jjwt)** — stateless, token-based authentication
- **Groq API** — LLM-powered job description parsing
- **Lombok** — boilerplate reduction
- **Docker, Railway** — deployment

## Architecture

The project follows a layered architecture:

```
Controller  → handles HTTP requests/responses only
Service     → business logic, orchestration, validation
Repository  → data access (Spring Data JPA)
Entity      → database schema
```

External integrations (Groq) are isolated in their own `client` package, kept separate from business logic — if the LLM provider ever changed, only that layer would need to change.

Errors are handled through a global `@ControllerAdvice` exception handler, returning consistent JSON error responses (status, error type, message, timestamp) instead of raw stack traces.

Authentication is stateless JWT: a custom filter validates the token on every request and sets the authenticated user in the security context. All `JobApplication` data is scoped to the authenticated user — verified at the service layer, not just trusted from client input.

## API Endpoints

### Auth
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/users/register` | Register a new user |
| POST | `/api/users/login` | Log in, returns a JWT token |
| GET | `/api/users/{id}` | Get user by id |

### Job Applications
_All endpoints below require a valid JWT (`Authorization: Bearer <token>`)._

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/applications` | Create a job application manually |
| GET | `/api/applications/{id}` | Get a single application (owner only) |
| PUT | `/api/applications/{id}/status?newStatus=APPLIED` | Update application status |
| GET | `/api/applications/mine` | Get all applications for the logged-in user |
| GET | `/api/applications/search?company=google` | Search by company name |
| GET | `/api/applications/filter?status=APPLIED` | Filter by status |
| POST | `/api/applications/parse` | Paste JD text, get back extracted fields (not saved yet) |
| POST | `/api/applications/confirm` | Save the (reviewed/edited) extracted fields as a new application |

## Try it live

No setup needed — hit the live API directly with a tool like Postman:

1. `POST https://trackhire-production-770a.up.railway.app/api/users/register`
2. `POST https://trackhire-production-770a.up.railway.app/api/users/login` → copy the returned token
3. Use `Authorization: Bearer <token>` on any `/api/applications/*` request

## Local setup

### Prerequisites
- Java 17+
- MySQL running locally
- A [Groq API key](https://console.groq.com) (free tier)

### Steps

1. Clone the repo
   ```
   git clone https://github.com/harigovindmelath/TrackHire.git
   ```
2. Create the database
   ```sql
   CREATE DATABASE trackhire_db;
   ```
3. Set the following environment variables:
   ```
   DB_USERNAME=your_mysql_username
   DB_PASSWORD=your_mysql_password
   GROQ_API_KEY=your_groq_api_key
   JWT_SECRET=any_long_random_string
   ```
4. Run the application
   ```
   mvn spring-boot:run
   ```
   The API will be available at `http://localhost:8080`.

## Example flow

1. `POST /api/users/register` — create an account
2. `POST /api/users/login` — get a JWT token
3. `POST /api/applications/parse` — paste a job description, get structured fields back
4. `POST /api/applications/confirm` — review, then save it as a real application
5. `PUT /api/applications/{id}/status?newStatus=APPLIED` — update status as you progress

## Future improvements

- Interview, Reminder, and Tag entities for deeper application tracking
- File attachments (resumes, offer letters)
- Dashboard analytics (applications by month, most-required skills)
- React frontend
