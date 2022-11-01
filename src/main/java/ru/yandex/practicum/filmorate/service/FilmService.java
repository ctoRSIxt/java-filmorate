package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmUnknownException;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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

        Film film = filmStorage.findFilmById(filmId);
        return film;
    }

    public Film create(Film film) {
        validateFilm(film);
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validateFilm(film);
        return filmStorage.update(film);
    }

    public Film addLike(long id, long userId) {
        Film film = filmStorage.findFilmById(id);
        User user = userStorage.findUserById(userId);
        filmStorage.addLike(film, user);
        return film;
    }

    public Film removeLike(long id, long userId) {
        Film film = filmStorage.findFilmById(id);
        User user = userStorage.findUserById(userId);
        filmStorage.removeLike(film, user);
        return film;
    }

    public List<Film> getTopFilmsByLikes(long count) {
        return filmStorage.findAll().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validateFilm(Film film) {

        if (film.getName().isBlank()) {
            log.info("Film: Валидация не пройдена: пустое назание");
            throw new ValidationException("Название не может быть пустым.");
        }

        if (film.getDescription().length() > 200) {
            log.info("Film: Валидация не пройдена: описание длиннее 200 символов");
            throw new ValidationException("Максимальная длина описание 200 символов.");
        }

        LocalDate firstRelease = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(firstRelease)) {
            log.info("Film: Валидация не пройдена: дата релиза раньше 28.12.1895");
            throw new ValidationException("Выход фильма не может быть раньше 28.12.1895.");
        }

        if (film.getDuration() < 0) {
            log.info("Film: Валидация не пройдена: продолжительность фильма отрицательна");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }
}


