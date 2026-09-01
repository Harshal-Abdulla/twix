# Twix

A Twitter-style microblogging application, built by a team of five as an MSc
group project at Maynooth University in December 2024.

Users search for a person, follow and unfollow them, post tweets up to 250
characters, and read a feed of the people they follow. The interface was
modelled on Twitter's, which gave five people one reference to build against
instead of designing it from scratch and arguing about it.

## How it is built

- **`backend/`** — Java 21, Spring Boot 3.4, PostgreSQL through Spring Data JPA.
  A REST API with modules for users, tweets and followers, plus an
  implementation of the observer pattern so followers are notified when someone
  they follow posts.
- **`frontend/`** — React, TypeScript and Vite, using Axios and Bootstrap. The
  feed refreshes by polling the API every three seconds.

Both directories keep their original commit history, so the branch merges and
who wrote what are still visible.

## Running it

Backend, from `backend/`:

```
cp src/main/resources/application.properties.example src/main/resources/application.properties
# fill in your database details, then:
./mvnw spring-boot:run
```

Or set `TWIX_DB_URL`, `TWIX_DB_USER` and `TWIX_DB_PASSWORD` in the environment
instead of editing the file.

Frontend, from `frontend/`:

```
npm install
npm run dev
```

## A note on credentials

The original history carried a live PostgreSQL password for a cloud database.
It has been removed from every commit, the file is no longer tracked, and that
database no longer exists. Copy the `.example` and keep your own values local.
