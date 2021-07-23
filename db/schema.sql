CREATE TABLE post (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE candidate (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE photo (
    id SERIAL PRIMARY KEY,
    path TEXT,
    candidate_id integer references candidate(id) on delete cascade
);

CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL
);