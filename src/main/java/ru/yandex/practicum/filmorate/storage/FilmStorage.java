package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;

public interface FilmStorage {

    // Return list of all films
    public Collection<Film> findAll();

    // Create a new film
    public Film create(Film film);

    // Update an existing film
    public Film update(Film film);

}
