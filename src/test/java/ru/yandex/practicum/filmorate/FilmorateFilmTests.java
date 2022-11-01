package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateFilmTests {
    private final FilmDbStorage filmStorage;

    Film film1;
    Film film2;

    @BeforeEach
    public void TestFilms() {
        film1 = new Film(1, "Фильм"
                , "Описание"
                , 120
                , LocalDate.of(1999, 12, 1)
                , new Mpa(1, "R"), new ArrayList<>(), new HashSet<>());

        film2 = new Film(2, "Фильм2"
                , "Описание2"
                , 100
                , LocalDate.of(1994, 3, 1)
                ,new Mpa(2,  "PG"), new ArrayList<>(), new HashSet<>());

    }

    @Test
    public void testFindFilmById() {
        filmStorage.create(film1);
        filmStorage.create(film2);
        Film testFilm1 = filmStorage.findFilmById(1);
        Film testFilm2 = filmStorage.findFilmById(2);
        assertThat(testFilm1).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(testFilm2).hasFieldOrPropertyWithValue("id", 2L);
    }

    @Test
    public void testFindAllFilms() {
        filmStorage.create(film1);
        filmStorage.create(film2);

        List<Film> films = filmStorage.findAll();
        assertThat(films).hasSize(2);
    }

    @Test
    public void testUpdateFilm() {
        filmStorage.create(film1);
        filmStorage.create(film2);
        Film film = filmStorage.findFilmById(1);

        String newName = "Film Title";
        film.setName(newName);
        filmStorage.update(film);
        Film updatedFilm = filmStorage.findFilmById(1);
        assertThat(updatedFilm).hasFieldOrPropertyWithValue("name", newName);
    }
}