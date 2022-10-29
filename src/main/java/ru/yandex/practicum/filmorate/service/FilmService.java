package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmUnknownException;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film findById(long filmId) {

        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new FilmUnknownException("No film with id =" + filmId);
        }

        return film;
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film addLike(long id, long userId) {

        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new FilmUnknownException("No film with id =" + id);
        }

        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new UserUnknownException("No user with id = " + userId);
        }

        film.getLikes().add(user.getId());
        filmStorage.update(film);
        return film;
    }

    public Film removeLike(long id, long userId) {
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new FilmUnknownException("No film with id = " + id);
        }

        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new UserUnknownException("No user with id = " + userId);
        }

        film.getLikes().remove(user.getId());
        filmStorage.update(film);
        return film;
    }

    public List<Film> getTopFilmsByLikes(long count) {
        return filmStorage.findAll().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}


