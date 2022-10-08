package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmUnknownException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController("/films")
public class FilmController {

    private static long idCounter = 0;
    private final Map<Long, Film> films = new HashMap<>();

    private FilmStorage filmStorage;
    private FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping(value = "/films")
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @GetMapping(value = "/films/{filmId}")
    public Film findById(@PathVariable long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) {
        return filmStorage.update(film);
    }

//    @PutMapping(value = "/films/{id")

}
