package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@RestController("/films")
public class FilmController {

    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping(value = "/films")
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) {

        log.info("Создание (post) записи для фильма {}", film.getName());

        filmValidator(film);

        if (films.containsKey(film.getId())) {
            log.info("Film with id = {} already exists.", film.getId());
            throw new FilmAlreadyExistException("Film with id = " + film.getId() + " already exists.");
        }
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {

        log.info("Редактирование (put) записи для фильма {}", film.getName());

        filmValidator(film);
        films.put(film.getId(), film);
        return film;
    }

    private void filmValidator(Film film) {

        if (film.getName().isBlank()) {
            log.info("Валидация не пройдена: пустое назание");
            throw new ValidationException("Название не может быть пустым.");
        }

        if (film.getDescription().length() > 200) {
            log.info("Валидация не пройдена: описание длиннее 200 символов");
            throw new ValidationException("Максимальная длина описание 200 символов.");
        }

        LocalDate firstRelease = LocalDate.of(1895,12,28);
        if (film.getReleaseDate().isBefore(firstRelease)) {
            log.info("Валидация не пройдена: дата релиза раньше 28.12.1895");
            throw new ValidationException("Выход фильма не может быть раньше 28.12.1895.");
        }

        if (film.getDuration().isNegative()) {
            log.info("Валидация не пройдена: продолжительность фильма отрицательна");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }

}
