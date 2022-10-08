package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    void addLike(Film film, User user) {
        film.getLikes().add(user.getId());
    }
    void removeLike(Film film, User user) {
        film.getLikes().remove(user.getId());
    }

    List<Film> getTop10ByLikes(Collection<Film> films) {
        return films.stream()
                .sorted((f1, f2) -> f1.getLikes().size() - f2.getLikes().size())
                .limit(10)
                .collect(Collectors.toList());
    }

}


