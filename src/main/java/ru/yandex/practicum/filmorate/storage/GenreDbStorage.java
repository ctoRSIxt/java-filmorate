package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Genre queryGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getLong("genre_id"),rs.getString("genre_name"));
    }

    @Override
    public List<Genre> findAll() {
        String getAllSql = "select * from genres order by genre_id";
        return jdbcTemplate.query(getAllSql, (rs, rowNum) -> queryGenre(rs));
    }

    @Override
    public Genre findById(long id) {
        String getByIdSql = "select * from genres where genre_id = ? order by genre_id";
        List<Genre> genres = jdbcTemplate.query(getByIdSql, (rs, rowNum) -> queryGenre(rs), id);

        if (genres.size() > 0) {
            return jdbcTemplate.query(getByIdSql, (rs, rowNum) -> queryGenre(rs), id).get(0);
        }

        return null;
    }

    @Override
    public Genre create(Genre genre) {

        String sqlPutGenre = "insert into genres (genre_name)" +
                "select ?" +
                "where not exists (select 1 from genres where genre_name = ?)";

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
        return genre;
    }

    @Override
    public Genre update(Genre genre) {

        String sqlGenreUpdate = "update genres set genre_name = ? where genre_id = ?";
        jdbcTemplate.update(sqlGenreUpdate
                , genre.getName()
                , genre.getId());

        return genre;
    }

    @Override
    public void remove(Genre genre) {
        String removeSql = "delete from genres where genre_id = ?";
        jdbcTemplate.update(removeSql, genre.getId());
    }

}
