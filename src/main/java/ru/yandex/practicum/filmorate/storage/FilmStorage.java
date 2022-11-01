package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmStorage {

    // Return list of all films
    public List<Film> findAll();

    // Return film by id
    public Film findFilmById(long id);

    // Create a new film
    public Film create(Film film);

    // Update an existing film
    public Film update(Film film);

    // Add likes
    public void addLike(Film film, User user);

    // Remove likes
    public void removeLike(Film film, User user);

}
