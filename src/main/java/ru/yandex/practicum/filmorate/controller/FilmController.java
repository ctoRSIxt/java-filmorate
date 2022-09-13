package ru.yandex.practicum.filmorate.controller;


import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController("/films")
public class FilmController {

    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {

        filmValidator(film);

        if (films.containsKey(film.getId())) {
            throw new FilmAlreadyExistException("Film with id = " + film.getId() + " already exists.");
        }
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) {

        filmValidator(film);
        films.put(film.getId(), film);
        return film;
    }

    private void filmValidator(Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым.");
        }

        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описание 200 символов.");
        }

        LocalDate firstRelease = LocalDate.of(1895,12,28);
        if (film.getReleaseDate().isBefore(firstRelease)) {
            throw new ValidationException("Выход фильма не может быть раньше " + firstRelease);
        }

        if (film.getDuration().isNegative()) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }

}
