package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;
    private final PostDao postDao;

    public UserDbStorage(JdbcTemplate jdbcTemplate, UserDao userDao, PostDao postDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
        this.postDao = postDao;
    }


    @Override
    public List<User> findAll() {

    };

    @Override
    public User getUserById(long id) {

    };

    @Override
    public User create(User user) {

    };

    @Override
    public User update(User user) {

    };
}
