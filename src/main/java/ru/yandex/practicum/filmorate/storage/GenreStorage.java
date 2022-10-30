package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    // Return list of all genres
    public List<Genre> findAll();

    // Return genre by id
    public Genre findById(long id);

    // Create a new genre
    public Genre create(Genre genre);

    // Update an existing genre
    public Genre update(Genre genre);

    // Remove genre
    public void remove(Genre genre);

}
