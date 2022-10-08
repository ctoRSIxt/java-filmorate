package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmUnknownException;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController("/films")
public class FilmController {

    private static long idCounter = 0;
    private final Map<Long, Film> films = new HashMap<>();

    private FilmStorage filmStorage;
    private FilmService filmService;
    private UserStorage userStorage;

    @Autowired
    public FilmController(FilmStorage filmStorage
                        , FilmService filmService
                        , UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
        this.userStorage = userStorage;
    }

    @GetMapping(value = "/films")
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @GetMapping(value = "/films/{filmId}")
    public Film findById(@PathVariable long filmId) {

        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new FilmUnknownException("No film with id =" + filmId);
        }

        return film;
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) {
        return filmStorage.create(film);
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) {
        return filmStorage.update(film);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public Film addLike(@PathVariable long id, @PathVariable long userId) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw  new FilmUnknownException("No film with id =" + id);
        }

        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new UserUnknownException("No user with id = " + userId);
        }
        filmService.addLike(film, user);
        return film;
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public Film removeLike(@PathVariable long id, @PathVariable long userId) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new FilmUnknownException("No film with id = " + id);
        }

        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new UserUnknownException("No user with id = " + userId);
        }

        filmService.removeLike(film, user);
        return film;
    }


    @GetMapping(value = "/films/popular")
    List<Film> getTopFilmsByLikes(@RequestParam(name = "count", required=false, defaultValue = "10") Long count) {
        return filmService.getTopFilmsByLikes(filmStorage.findAll(), count);
    }




}
