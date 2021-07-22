CREATE TABLE post (
    id SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE candidate (
    id SERIAL PRIMARY KEY,
    name TEXT
);

CREATE TABLE photo (
    id SERIAL PRIMARY KEY,
    path TEXT,
    candidate_id integer references candidate(id) on delete cascade
);

CREATE TABLE user (
    id SERIAL PRIMARY KEY,
    name TEXT,
    email TEXT NOT NULL UNIQUE,
    password TEXT
);