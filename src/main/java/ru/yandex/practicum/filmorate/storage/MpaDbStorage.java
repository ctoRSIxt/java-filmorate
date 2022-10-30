package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Component
@Slf4j
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Mpa queryMpa(ResultSet rs) throws SQLException {
        return new Mpa(rs.getLong("mpa_id"),rs.getString("mpa_name"));
    }

    @Override
    public List<Mpa> findAll() {
        String getAllSql = "select * from mpas order by mpa_id";
        return jdbcTemplate.query(getAllSql, (rs, rowNum) -> queryMpa(rs));
    }

    @Override
    public Mpa findById(long id) {

        String getByIdSql = "select * from mpas where mpa_id = ? order by mpa_id";
        List<Mpa> mpas = jdbcTemplate.query(getByIdSql, (rs, rowNum) -> queryMpa(rs), id);
        if (mpas.size() > 0) {
            return jdbcTemplate.query(getByIdSql, (rs, rowNum) -> queryMpa(rs), id).get(0);
        }
        return null;
    }

    @Override
    public Mpa create(Mpa mpa) {

        validateMpa(mpa);

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
        mpa.setId(keyHolder.getKey().longValue());
        return mpa;
    }

    @Override
    public Mpa update(Mpa mpa) {

        validateMpa(mpa);

        String sqlUpdate = "update mpas set mpa_name = ? where mpa_id = ?";
        jdbcTemplate.update(sqlUpdate
                , mpa.getName()
                , mpa.getId());

        return mpa;
    }

    @Override
    public void remove(Mpa mpa) {
        String removeSql = "delete from mpas where mpa_id = ?";
        jdbcTemplate.update(removeSql, mpa.getId());
    }

    private void validateMpa(Mpa mpa) {

        if (mpa == null) {
            log.info("Mpa: Валидация не пройдена: возвращается null");
            throw new ValidationException("mpa рейтинг не может быть пустым");

        }

        if (mpa.getName() == null) {
            log.info("Mpa: Валидация не пройдена: mpa имя не может быть пустым");
            mpa.setName("Unknown");
//            throw new ValidationException("mpa имя не может быть пустым");
        }

        if (mpa.getId() < 0) {
            log.info("Mpa: Валидация не пройдена: id не может быть отрицательным");
            throw new ValidationException("id не может быть отрицательным");
        }
    }


}
