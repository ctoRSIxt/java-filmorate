package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("UserDbStorage")
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<User> findAll() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> queryUser(rs));
    };


    @Override
    public User findUserById(long id) {

        String sql = "select * from users where user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, id);

        if(userRows.next()) {
            User user = new User(userRows.getLong("user_id")
                    ,userRows.getString("email")
                    ,userRows.getString("login")
                    ,userRows.getString("name")
                    ,userRows.getDate("birthday").toLocalDate());

            user.setFriends(getFriends(user.getId()));
            return user;
        } else {
            return null;
        }

    };


    @Override
    public User create(User user) {

        validateUser(user);

        String sqlUsers = "insert into users(email, login, name, birthday) " +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlUsers, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, java.sql.Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        updateFriends(user);
        return user;

    };

    @Override
    public User update(User user) {

        validateUser(user);

        String sql = "select * from users where user_id = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sql, user.getId());

        if (!userRows.next()) {
            throw new UserUnknownException("Пользователь с id = " + user.getId() + " не известен.");
        }


        String sqlUserUpdate = "update users set " +
                "email = ?, login = ?, name = ?, " +
                "birthday = ? where user_id = ?";
        jdbcTemplate.update(sqlUserUpdate
                , user.getEmail()
                , user.getLogin()
                , user.getName()
                , java.sql.Date.valueOf(user.getBirthday())
                , user.getId());

        updateFriends(user);
        return user;
    };

    private User queryUser(ResultSet rs) throws SQLException {
        User user = new User(rs.getLong("user_id")
                ,rs.getString("email")
                ,rs.getString("login")
                ,rs.getString("name")
                ,rs.getDate("birthday").toLocalDate());

        user.setFriends(getFriends(user.getId()));
        return user;
    }

    private Map<Long, Boolean> getFriends(long user_id) {
        String sql = "select * from user_friendship where user_id = ?";
        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet(sql, user_id);

        Map<Long, Boolean> friends = new HashMap<>();
        while (friendsRows.next()) {
            friends.put(friendsRows.getLong("friend_id"),
                        friendsRows.getBoolean("accepted"));
        }
        return friends;
    }

    private void updateFriends(User user) {
        String sqlInsFriends = "update user_friendship SET accepted=? where user_id = ? and friend_id = ?;"+
                "insert into user_friendship(user_id, friend_id, accepted)" +
                "select ?, ?, ?" +
                "where not exists (select 1 from user_friendship where user_id = ? and friend_id = ?)";
        for (Long friendId : user.getFriends().keySet()) {
            long userId = user.getId();
            boolean ac = user.getFriends().get(friendId);
            jdbcTemplate.update(sqlInsFriends
                    ,ac, userId, friendId
                    ,userId, friendId, ac
                    ,userId, friendId);
        }

        for (Long dbFriend : getFriends(user.getId()).keySet()) {
            if (!user.getFriends().containsKey(dbFriend)) {
                removeFriend(user.getId(), dbFriend);
            }
        }
    }

    private void removeFriend(Long userId, Long friendId) {
        String sqlRemFriends = "delete from user_friendship where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sqlRemFriends, userId, friendId);
    }
    

    private void validateUser(User user) {

        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.info("User: Валидация не пройдена: email пуст или не содержит @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать @");
        }

        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.info("User: Валидация не пройдена: логин пуст или содержит пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.info("User: Имя пользователя пустое: будет использоваться логин");
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("User: Валидация не пройдена: дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
