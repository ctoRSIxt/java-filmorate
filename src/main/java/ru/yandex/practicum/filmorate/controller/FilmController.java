package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@Slf4j
@RestController("/films")
public class FilmController {

    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping(value = "/films")
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @GetMapping(value = "/films/{filmId}")
    public Film findById(@PathVariable long filmId) {
        return filmService.findById(filmId);
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) {
        return filmService.update(film);
    }

    @PutMapping(value = "/films/{id}/like/{userId}")
    public Film addLike(@PathVariable long id, @PathVariable long userId) {
        return filmService.addLike(id, userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}")
    public Film removeLike(@PathVariable long id, @PathVariable long userId) {
        return filmService.removeLike(id, userId);
    }


    @GetMapping(value = "/films/popular")
    List<Film> getTopFilmsByLikes(@RequestParam(name = "count", required=false, defaultValue = "10") Long count) {
        return filmService.getTopFilmsByLikes(count);
    }




}
