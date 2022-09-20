package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController("/users")
public class UserController {
    private static long idCounter = 0;
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping(value = "/users")
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) {

        log.info("Создание (post) записи для пользователя {}", user.getName());

        validateUser(user);

        user.setId(++idCounter);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping(value = "/users")
    public User put(@RequestBody User user) {

        log.info("Редактирование (put) записи для пользователя {}", user.getName());
        validateUser(user);

        if(!users.containsKey(user.getId())) {
            throw new UserUnknownException("Пользователь с id = " + user.getId() + " не известен.");
        }

        users.put(user.getId(), user);

        return user;
    }

    private void validateUser(User user) {

        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.info("Валидация не пройдена: email пуст или не содержит @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать @");
        }

        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.info("Валидация не пройдена: логин пуст или содержит пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Имя пользователя пустое: будет использоваться логин");
            user.setName(user.getLogin());
        }
        LocalDate firstRelease = LocalDate.now();
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Валидация не пройдена: дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

    }

}
