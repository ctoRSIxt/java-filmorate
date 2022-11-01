package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;


import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmorateMpaTests {
    private final MpaDbStorage mpaStorage;

    @Test
    public void testFindMpaById() {
        Optional<Mpa> testMpa1 = mpaStorage.findById(1);
        Optional<Mpa> testMpa2 = mpaStorage.findById(2);

        assertThat(testMpa1)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "G")
                );

        assertThat(testMpa2)
                .isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "PG")
                );
    }

    @Test
    public void testFindAllMpas() {
        List<Mpa> genres = mpaStorage.findAll();
        assertThat(genres).hasSize(5);
    }

    @Test
    public void testUpdateMpa() {
        Optional<Mpa> mpa = mpaStorage.findById(1);

        String newName = "NC-175";
        mpa.get().setName(newName);
        mpaStorage.update(mpa.get());
        Optional<Mpa> updatedMpa = mpaStorage.findById(1);

        assertThat(updatedMpa)
                .isPresent()
                .hasValueSatisfying(mpa1 ->
                        assertThat(mpa1).hasFieldOrPropertyWithValue("name", newName)
                );
    }
}
