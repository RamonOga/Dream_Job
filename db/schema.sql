CREATE TABLE post (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);


CREATE TABLE city (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE candidate (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL,
    city_id integer references city(id) on delete cascade
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

CREATE TABLE city (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);

CREATE TABLE candidate_visitors (
    id SERIAL PRIMARY KEY,
    count integer,
    candidate_id integer references candidate(id) on delete cascade
);

