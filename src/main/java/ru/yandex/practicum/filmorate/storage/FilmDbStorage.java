package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmUnknownException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component("FilmDbStorage")
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> findAll() {
        String sql = "select * from films";
        return jdbcTemplate.query(sql, (rs, rowNum) -> queryFilm(rs));
    };

    @Override
    public Film findFilmById(long id) {
        String sql = "select f.film_id as film_id, f.name as name, f.description as description, " +
                "f.duration as duration, f.release_date as release_date, " +
                "m.mpa_id as mpa_id, m.mpa_name as mpa_name from films as f " +
                "left outer join film_mpas as fm on fm.film_id = f.film_id " +
                "left outer join mpas as m on fm.mpa_id = m.mpa_id " +
                "where f.film_id = ?";


        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sql, id);

        if(filmRows.next()) {
            Film film = new Film(filmRows.getLong("film_id")
                    ,filmRows.getString("name")
                    ,filmRows.getString("description")
                    ,filmRows.getInt("duration")
                    ,filmRows.getDate("release_date").toLocalDate()
                    ,null, new ArrayList<>(), new HashSet<>());

            Mpa mpa = new Mpa(filmRows.getLong("mpa_id"), filmRows.getString("mpa_name"));
            film.setLikes(new HashSet<>(getLikes(film.getId())));
            film.setGenres(getGenres(film.getId()));
            film.setMpa(mpa);

            return film;
        } else {
            throw new FilmUnknownException("No film with id =" + id);
        }

    };

    @Override
    public Film create(Film film) {
        if (film.getMpa() == null) {
            throw new ValidationException("mpa рейтинг не может быть пустым");
        };

        validateFilm(film);

        String sqlFilms = "insert into films(name, description, duration, release_date) " +
                "values (?, ?, ?, ?)";


        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlFilms, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setInt(3, film.getDuration());
            stmt.setDate(4, java.sql.Date.valueOf(film.getReleaseDate()));
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());

        updateFilmGenres(film);
        updateFilmMpas(film);
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
                "name = ?, description = ?, duration = ?, " +
                "release_date = ?" +
                "where film_id = ?";
        jdbcTemplate.update(sqlFilmUpdate
                , film.getName()
                , film.getDescription()
                , film.getDuration()
                , java.sql.Date.valueOf(film.getReleaseDate())
                , film.getId());

        updateFilmGenres(film);
        updateFilmMpas(film);
        updateFilmLikes(film);


        return film;

    };


    private Film queryFilm(ResultSet rs) throws SQLException {
        Film film = new Film(rs.getLong("film_id")
                        ,rs.getString("name")
                        ,rs.getString("description")
                        ,rs.getInt("duration")
                        ,rs.getDate("release_date").toLocalDate()
                        ,null, new ArrayList<>(), new HashSet<>());

        film.setLikes(new HashSet<>(getLikes(film.getId())));
        film.setGenres(getGenres(film.getId()));
        film.setMpa(getMpa(film.getId()));
        return film;
    }

    private List<Long> getLikes(long film_id) {
        String sql = "select user_id from film_likes where film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> queryLike(rs), film_id);
    }

    private Long queryLike(ResultSet rs) throws SQLException {
        return rs.getLong("user_id");
    }

    private Mpa getMpa(long film_id) {
        String sql = "select m.mpa_id as mpa_id, m.mpa_name as mpa_name from film_mpas as fm " +
                "left outer join mpas as m ON fm.mpa_id = m.mpa_id " +
                "where fm.film_id = ?";

        List<Mpa> mpas = jdbcTemplate.query(sql, (rs, rowNum) -> queryMpa(rs), film_id);
        if (mpas.size() > 0) {
            return mpas.get(0);
        }
        return null;
    }

    private Mpa queryMpa(ResultSet rs) throws SQLException {
        return new Mpa(rs.getLong("mpa_id"), rs.getString("mpa_name"));
    }

    private List<Genre> getGenres(long film_id) {
        String sql = "select g.genre_id as genre_id, g.genre_name as genre_name from film_genres as fg " +
                "left outer join genres as g ON fg.genre_id = g.genre_id " +
                "where fg.film_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> queryGenre(rs), film_id);

    }


    private Genre queryGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getLong("genre_id"),rs.getString("genre_name"));
    }

    private void updateGenres(Film film) {
        if (film.getGenres() != null) {

            String sqlPutGenre = "insert into genres (genre_name)" +
                    "select ?" +
                    "where not exists (select 1 from genres where genre_name = ?)";

            for (Genre genre : film.getGenres()) {
                KeyHolder keyHolder = new GeneratedKeyHolder();
                jdbcTemplate.update(connection -> {
                    PreparedStatement stmt = connection.prepareStatement(sqlPutGenre, new String[]{"genre_id"});
                    stmt.setString(1, genre.getName());
                    stmt.setString(2, genre.getName());
                    return stmt;
                }, keyHolder);

                if (keyHolder.getKey() != null) {
                    genre.setId(keyHolder.getKey().longValue());
                }
            }
        }
    }


    private void updateFilmGenres(Film film) {
        String sqlRemFilmGenres = "delete from film_genres where film_id = ?";
        jdbcTemplate.update(sqlRemFilmGenres, film.getId());

        String sqlInsFilmGenres = "insert into film_genres(film_id, genre_id)" +
                "select ?, ?" +
                "where not exists (select 1 from film_genres where film_id = ? and genre_id = ?)";

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlInsFilmGenres
                        , film.getId(), genre.getId()
                        , film.getId(), genre.getId());
            }
        }
        film.setGenres(getGenres(film.getId()));
    }

    private void updateMpa(Film film) {
        Mpa mpa = film.getMpa();
        String sqlPut = "insert into mpas (mpa_name)" +
                "select ?" +
                "where not exists (select 1 from mpas where mpa_name = ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlPut, new String[]{"mpa_id"});
            stmt.setString(1, mpa.getName());
            stmt.setString(2, mpa.getName());
            return stmt;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            mpa.setId(keyHolder.getKey().longValue());
        }
    }

    private void updateFilmMpas(Film film) {
        if (film.getMpa() == null) return;

        String sqlInsFilmMpas = "update film_mpas SET mpa_id = ? where film_id = ?;"+
                "insert into film_mpas(film_id, mpa_id)" +
                "select ?, ?" +
                "where not exists (select 1 from film_mpas where film_id = ? and mpa_id = ?)";

        jdbcTemplate.update(sqlInsFilmMpas
                , film.getMpa().getId(), film.getId()
                , film.getId(), film.getMpa().getId()
                , film.getId(), film.getMpa().getId());


        String getByIdSql = "select * from mpas where mpa_id = ? order by mpa_id";
        List<Mpa> mpas = jdbcTemplate.query(getByIdSql, (rs, rowNum) -> queryMpa(rs)
                , film.getMpa().getId());

        if (mpas.size() > 0) {
            film.setMpa(mpas.get(0));
        }

    }

    private void updateFilmLikes(Film film) {
        String sqlInsFilmLikes = "insert into film_likes(film_id, user_id)" +
                "select ?, ?" +
                "where not exists (select 1 from film_likes where film_id = ? and user_id = ?)";

        if (film.getLikes() != null) {
            for (Long userId : film.getLikes()) {
                jdbcTemplate.update(sqlInsFilmLikes
                        , film.getId(), userId, film.getId(), userId);
            }
        }

        if (film.getLikes() != null) {
            for (Long userId : getLikes(film.getId())) {
                if (!film.getLikes().contains(userId)) {
                    removeLike(film.getId(), userId);
                }
            }
        }

    }

    private void removeLike(Long filmId, Long userId) {
        String sqlRemLike = "delete from film_likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlRemLike, filmId, userId);
    }


    private void validateFilm(Film film) {

        if (film.getName().isBlank()) {
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
