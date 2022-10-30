package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmorateGenreTests {
    private final GenreDbStorage genreStorage;

    Genre genre1;
    Genre genre2;

    @BeforeEach
    public void TestGenre() {
        genre1 = new Genre(1, "Комедия");
        genre2 = new Genre(2, "Драма");
        genreStorage.create(genre1);
        genreStorage.create(genre2);

    }

    @Test
    public void testFindGenreById() {
        Genre testGenre1 = genreStorage.findById(1);
        Genre testGenre2 = genreStorage.findById(2);
        assertThat(testGenre1).hasFieldOrPropertyWithValue("name", "Комедия");
        assertThat(testGenre2).hasFieldOrPropertyWithValue("name", "Драма");
    }

    @Test
    public void testFindAllGenres() {
        List<Genre> genres = genreStorage.findAll();
        assertThat(genres).hasSize(2);
    }

    @Test
    public void testUpdateGenre() {
        Genre genre = genreStorage.findById(1);

        String newName = "COMEDY";
        genre.setName(newName);
        genreStorage.update(genre);
        Genre updatedGenre = genreStorage.findById(1);
        assertThat(updatedGenre).hasFieldOrPropertyWithValue("name", newName);
    }
}
