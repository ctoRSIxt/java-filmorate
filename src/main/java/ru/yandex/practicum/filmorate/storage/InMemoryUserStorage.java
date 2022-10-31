package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private static long idCounter = 0;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findUserById(long id) {
        User user = users.get(id);
        if (user == null) {
            throw new UserUnknownException("No user with id = " + id);
        }
        return user;
    }

    @Override
    public User create(User user) {
        log.info("Создание (post) записи для пользователя {}", user.getName());

        user.setId(++idCounter);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        log.info("Редактирование (put) записи для пользователя {}", user.getName());

        if (!users.containsKey(user.getId())) {
            throw new UserUnknownException("Пользователь с id = " + user.getId() + " не известен.");
        }

        users.put(user.getId(), user);

        return user;
    }

}
