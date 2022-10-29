DROP TABLE IF EXISTS FILM_GENRES;
DROP TABLE IF EXISTS FILM_LIKES;
DROP TABLE IF EXISTS FILMS;
DROP TABLE IF EXISTS GENRES;
DROP TABLE IF EXISTS USER_FRIENDSHIP;
DROP TABLE IF EXISTS USERS;


CREATE TABLE IF NOT EXISTS films (
    film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(40) NOT NULL,
    description  varchar(500) NOT NULL,
    duration INTEGER NOT NULL,
    release_date date,
    rating varchar(40)
);


CREATE TABLE IF NOT EXISTS users (
    user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email varchar(100) NOT NULL,
    login varchar(50) NOT NULL,
    name  varchar(100) NOT NULL,
    birthday date NOT NULL
);


CREATE TABLE IF NOT EXISTS genres (
    genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    genre_name varchar(100) UNIQUE NOT NULL
);


CREATE TABLE IF NOT EXISTS film_genres (
    film_id INTEGER NOT NULL REFERENCES films (film_id) ON DELETE RESTRICT,
    genre_id INTEGER NOT NULL REFERENCES genres (genre_id) ON DELETE RESTRICT
);


CREATE TABLE IF NOT EXISTS film_likes (
    film_id INTEGER NOT NULL REFERENCES films (film_id) ON DELETE RESTRICT,
    user_id INTEGER NOT NULL REFERENCES users (user_id) ON DELETE RESTRICT
);


CREATE TABLE IF NOT EXISTS user_friendship (
    user_id INTEGER NOT NULL REFERENCES users (user_id) ON DELETE RESTRICT,
    friend_id INTEGER NOT NULL REFERENCES users (user_id) ON DELETE RESTRICT,
    accepted BOOLEAN NOT NULL DEFAULT FALSE
);