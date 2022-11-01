package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.GenreUnknownException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.Collection;

@Slf4j
@RestController("/genres")
public class GenreController {

    private GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping(value = "/genres")
    public Collection<Genre> findAll() {
        return genreService.findAll();
    }

    @GetMapping(value = "/genres/{genreId}")
    public Genre findById(@PathVariable long genreId) {
        return genreService.findById(genreId).orElseThrow(
                () -> new GenreUnknownException("No genre with id =" + genreId));
    }

}
