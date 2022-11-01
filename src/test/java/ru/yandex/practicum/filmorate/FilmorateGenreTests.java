package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmorateGenreTests {
    private final GenreDbStorage genreStorage;

    @Test
    public void testFindGenreById() {
        Optional<Genre> testGenre1 = genreStorage.findById(1);
        Optional<Genre> testGenre2 = genreStorage.findById(2);

        assertThat(testGenre1)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия")
                );

        assertThat(testGenre2)
                .isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Драма")
                );
    }

    @Test
    public void testFindAllGenres() {
        List<Genre> genres = genreStorage.findAll();
        assertThat(genres).hasSize(6);
    }

    @Test
    public void testUpdateGenre() {
        Optional<Genre> genre = genreStorage.findById(1);

        String newName = "COMEDY";
        genre.get().setName(newName);
        genreStorage.update(genre.get());
        Optional<Genre> updatedGenre = genreStorage.findById(1);

        assertThat(updatedGenre)
                .isPresent()
                .hasValueSatisfying(genre1 ->
                        assertThat(genre1).hasFieldOrPropertyWithValue("name", newName)
                );
    }
}
