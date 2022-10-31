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

        validateUser(user);

        user.setId(++idCounter);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {

        log.info("Редактирование (put) записи для пользователя {}", user.getName());
        validateUser(user);

        if (!users.containsKey(user.getId())) {
            throw new UserUnknownException("Пользователь с id = " + user.getId() + " не известен.");
        }

        users.put(user.getId(), user);

        return user;
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
