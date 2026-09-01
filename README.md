# Twix

A real-time chat and microblogging application, built by a team of five as an
MSc group project at Maynooth University in December 2024.

The interface was modelled on Twitter's, which gave five people one reference to
build against instead of designing it from scratch and arguing about it.

- `backend/` — Java and Spring Boot over PostgreSQL, with WebSockets for live
  delivery. Includes an observer-pattern implementation for follower updates.
- `frontend/` — React, TypeScript and Vite, using Axios and Bootstrap.

Both directories keep their original commit history, so the branch merges and
who wrote what are still visible.

## Running it

The backend needs a PostgreSQL database. Copy
`backend/src/main/resources/application.properties.example` to
`application.properties` and fill in your own values, or set `TWIX_DB_URL`,
`TWIX_DB_USER` and `TWIX_DB_PASSWORD` in the environment.

**No credentials belong in this repository.** The original history contained a
database password; it has been removed and that database no longer exists.
