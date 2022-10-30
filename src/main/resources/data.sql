INSERT INTO mpas (mpa_name)
VALUES
    ('G'),
    ('PG'),
    ('PG-13'),
    ('R'),
    ('NC-17')
;

INSERT INTO genres (genre_name)
VALUES
    ('Комедия'),
    ('Драма'),
    ('Мультфильм'),
    ('Триллер'),
    ('Документальный'),
    ('Боевик')
;




-- INSERT INTO films (name, description, duration, release_date)
-- VALUES
--     ('The Godfather','The aging patriarch...',175,'1972-01-01'),
--     ('Pulp Fiction','The lives of two mob...',154,'1994-01-01'),
--     ('Eternal Sunshine of the Spotless Mind','When their relationship...',108,'2004-01-01'),
--     ('Pleasantville','Two 1990s teenage...',102,'1998-01-01'),
--     ('Back to the Future','Marty McFly, a 17-year...',116,'1985-01-01'),
--     ('Toy Story 3','The toys are mistakenly...',103,'2010-01-01'),
--     ('This Film Is Not Yet Rated','Kirby Dicks expose about...',98,'2006-01-01'),
--     ('Ghostbusters','Three parapsychologists...',105, '1984-01-01')
-- ;


--
-- INSERT INTO mpa (mpa_name)
-- VALUES
--     ('G'),
--     ('PG'),
--     ('PG-13'),
--     ('R'),
--     ('NC-17')
-- ;

-- INSERT INTO film_mpa (film_id, mpa_id)
-- VALUES
--     (1, 4),
--     (2, 4),
--     (3, 4),
--     (4, 3),
--     (5, 2),
--     (6, 1),
--     (7, 5),
--     (8, 1)
-- ;

-- INSERT INTO genres (genre_name)
-- VALUES
--     ('Crime'),
--     ('Drama'),
--     ('Romance'),
--     ('Sci-Fi'),
--     ('Comedy'),
--     ('Fantasy'),
--     ('Adventure'),
--     ('Animation'),
--     ('Documentary'),
--     ('Action')
-- ;
--
-- INSERT INTO film_genres (film_id, genre_id)
-- VALUES
--     (1,1),
--     (1,2),
--     (2,1),
--     (2,2),
--     (3,2),
--     (3,3),
--     (3,4),
--     (4,2),
--     (4,5),
--     (4,6),
--     (5,7),
--     (5,5),
--     (5,4),
--     (6,5),
--     (6,7),
--     (6,8),
--     (7,9),
--     (8,5),
--     (8,6),
--     (8,10)
-- ;
--
--
-- INSERT INTO users (email, login, name, birthday)
-- VALUES
--     ('vanya@gmail.com', 'ivan333', 'Ivan Ivanov', '2003-04-05'),
--     ('teee@mail.ru', 'ppetya', 'Petr Petrov', '1999-09-15'),
--     ('smirnova23@yandex.ru', 'n23smir', 'Nadya Smirnova', '2005-10-02'),
--     ('eglen95@rambler.ru', 'lenna5', 'Lena Egorova', '1995-12-22'),
--     ('orlov@mail.ru', 'valera', 'Valera Orlov', '2000-06-08')
-- ;
--
-- INSERT INTO user_friendship (user_id, friend_id, accepted)
-- VALUES
--     (1, 2, true),
--     (1, 3, false),
--     (1, 4, true),
--     (1, 5, true),
--     (2, 1, false),
--     (2, 4, true),
--     (3, 1, true),
--     (3, 4, true),
--     (3, 2, false),
--     (4, 1, false),
--     (4, 5, false),
--     (5, 3, false),
--     (5, 4, false)
-- ;
--
-- INSERT INTO film_likes (film_id, user_id)
-- VALUES
--     (1, 2),
--     (1, 3),
--     (2, 4),
--     (2, 5),
--     (3, 1),
--     (3, 2),
--     (4, 1),
--     (5, 1),
--     (5, 2),
--     (5, 4),
--     (6, 5),
--     (7, 3),
--     (8, 4)
-- ;


