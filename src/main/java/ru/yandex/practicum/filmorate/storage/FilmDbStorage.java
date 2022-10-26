package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmUnknownException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }



    private Film queryFilm(ResultSet rs) throws SQLException {
        Film film = new Film(rs.getLong("film_id")
                        ,rs.getString("title")
                        ,rs.getString("description")
                        ,rs.getInt("duration")
                        ,rs.getDate("release_date").toLocalDate()
                        ,rs.getString("rating"));

        film.setLikes(new HashSet<>(getLikes(film.getId())));
        film.setGenre(getGenres(film.getId()));
        return film;
    }

    private List<Long> getLikes(long film_id) {
        String sql = "select user_id from film_likes where film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> queryLike(rs), film_id);
    }

    private Long queryLike(ResultSet rs) throws SQLException {
        return rs.getLong("user_id");
    }

    private List<String> getGenres(long film_id) {
        String sql = "select g.genre_name as genre_name from film_genres as fg " +
                "left outer join genres as g ON fg.genre_id = g.genre_id " +
                "where fg.film_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> queryGenre(rs), film_id);

    }

    private String queryGenre(ResultSet rs) throws SQLException {
        return rs.getString("genre_name");
    }


    private void updateGenres(Film film) {

        // Inserts if genre_name doesn't exist
        String sqlPutGenre = "insert into genres (genre_name)" +
                "select ?" +
                "where not exists (select 1 from genres where genre_name = ?";

        for (String genre : film.getGenre()) {
            jdbcTemplate.update(sqlPutGenre,genre, genre);
        }
    }

    private void updateFilmGenres(Film film) {

        // Get genre_name to genre_id map
        String sqlGetGenre = "select * from genres";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlGetGenre);
        Map<String, Integer> genreMap = new HashMap<>();
        if(genreRows.next()) {
            genreMap.put(genreRows.getString("genre_name")
                    , genreRows.getInt("genre_id"));
        }

        // Update film_genres
        String sqlInsFilmGenres = "insert into film_genres(film_id, genre_id)" +
                                  "select ?, ?" +
        "where not exists (select 1 from film_genres where film_id = ? and genre_id = ?)";

        for (String genre : film.getGenre()) {
            jdbcTemplate.update(sqlInsFilmGenres
                    , film.getId(), genreMap.get(genre), film.getId(), genreMap.get(genre));
        }

    }

    private void updateFilmLikes(Film film) {
        String sqlInsFilmLikes = "insert into film_likes(film_id, user_id)" +
                "select ?, ?" +
                "where not exists (select 1 from film_likes where film_id = ? and user_id = ?)";

        for (Long userID : film.getLikes()) {
            jdbcTemplate.update(sqlInsFilmLikes
                    , film.getId(), userID, film.getId(), userID);
        }

    }



    @Override
    public List<Film> findAll() {
        String sql = "select * from films";
        return jdbcTemplate.query(sql, (rs, rowNum) -> queryFilm(rs));
    };

    @Override
    public Film getFilmById(long id) {
        String sql = "select * from films where film_id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, id);

        if(filmRows.next()) {
            Film film = new Film(filmRows.getLong("film_id")
                    ,filmRows.getString("title")
                    ,filmRows.getString("description")
                    ,filmRows.getInt("duration")
                    ,filmRows.getDate("release_date").toLocalDate()
                    ,filmRows.getString("rating"));

            film.setLikes(new HashSet<>(getLikes(film.getId())));
            film.setGenre(getGenres(film.getId()));

            return film;
        } else {
            return null;
        }

    };

    @Override
    public Film create(Film film) {

        validateFilm(film);

        String sqlFilms = "insert into films(title, description, duration, release_date, rating) " +
                "values (?, ?, ?, ?, ?)";


        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlFilms, new String[]{"film_id"});
            stmt.setString(1, film.getTitle());
            stmt.setString(2, film.getDescription());
            stmt.setInt(3, film.getDuration());
            stmt.setDate(4, java.sql.Date.valueOf(film.getReleaseDate()));
            stmt.setString(5, film.getRating());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());

        updateGenres(film);
        updateFilmGenres(film);
        updateFilmLikes(film);

        return film;
    };

    @Override
    public Film update(Film film) {

        validateFilm(film);

        String sql = "select * from films where film_id = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, film.getId());

        if (!filmRows.next()) {
            throw new FilmUnknownException("Фильм с id = " + film.getId() + " не известен.");
        }


        String sqlFilmUpdate = "update films set " +
                "title = ?, description = ?, duration = ?, " +
                "release_date = ?, rating = ?" +
                "where id = ?";
        jdbcTemplate.update(sqlFilmUpdate
                , film.getTitle()
                , film.getDescription()
                , film.getDuration()
                , java.sql.Date.valueOf(film.getReleaseDate())
                , film.getRating());

        updateGenres(film);
        updateFilmGenres(film);
        updateFilmLikes(film);


        return film;

    };


    private void validateFilm(Film film) {

        if (film.getTitle().isBlank()) {
            log.info("Film: Валидация не пройдена: пустое назание");
            throw new ValidationException("Название не может быть пустым.");
        }

        if (film.getDescription().length() > 200) {
            log.info("Film: Валидация не пройдена: описание длиннее 200 символов");
            throw new ValidationException("Максимальная длина описание 200 символов.");
        }

        LocalDate firstRelease = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(firstRelease)) {
            log.info("Film: Валидация не пройдена: дата релиза раньше 28.12.1895");
            throw new ValidationException("Выход фильма не может быть раньше 28.12.1895.");
        }

        if (film.getDuration() < 0) {
            log.info("Film: Валидация не пройдена: продолжительность фильма отрицательна");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }

}
