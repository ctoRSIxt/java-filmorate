package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaStorage {

    // Return list of all genres
    public List<Mpa> findAll();

    // Return genre by id
    public Mpa findById(long id);

    // Create a new genre
    public Mpa create(Mpa mpa);

    // Update an existing genre
    public Mpa update(Mpa mpa);

    // Remove genre
    public void remove(Mpa mpa);
}
