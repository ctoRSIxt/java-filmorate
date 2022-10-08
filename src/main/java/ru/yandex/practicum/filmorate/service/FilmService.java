package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {

    public void addLike(Film film, User user) {
        film.getLikes().add(user.getId());
    }
    public void removeLike(Film film, User user) {
        film.getLikes().remove(user.getId());
    }

    public List<Film> getTopFilmsByLikes(Collection<Film> films, long count) {
        return films.stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }

}


