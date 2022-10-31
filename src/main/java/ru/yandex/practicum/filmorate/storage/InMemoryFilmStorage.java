package ru.yandex.practicum.filmorate.storage;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmUnknownException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;


@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private static long idCounter = 0;
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film findFilmById(long id) {

        Film film = films.get(id);
        if (film == null) {
            throw new FilmUnknownException("No film with id =" + id);
        }
        return film;
    }

    @Override
    public Film create(Film film) {

        log.info("Создание (post) записи для фильма {}", film.getName());

        film.setId(++idCounter);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {

        log.info("Редактирование (put) записи для фильма {}", film.getName());

        if (!films.containsKey(film.getId())) {
            throw new FilmUnknownException("Фильм с id = " + film.getId() + " не известен.");
        }
        films.put(film.getId(), film);
        return film;
    }

}
