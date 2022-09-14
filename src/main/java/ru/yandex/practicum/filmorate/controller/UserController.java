package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController("/users")
public class UserController {
    private final HashMap<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) {

        log.info("Создание (post) записи для пользователя {}", user.getName());

        userValidator(user);

        if(users.containsKey(user.getId())) {
            throw new UserAlreadyExistException("Пользователь с электронной почтой " +
                    user.getEmail() + " уже зарегистрирован.");
        }
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping(value = "/users")
    public User put(@RequestBody User user) {

        log.info("Редактирование (put) записи для пользователя {}", user.getName());
        userValidator(user);
        users.put(user.getId(), user);

        return user;
    }

    private void userValidator(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.info("Валидация не пройдена: email пуст или не содержит @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать @");
        }

        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.info("Валидация не пройдена: логин пуст или содержит пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }

        if (user.getName().isBlank()) {
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
