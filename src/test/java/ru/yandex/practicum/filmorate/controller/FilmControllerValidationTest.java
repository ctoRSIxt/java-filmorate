package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerValidationTest {

    FilmController filmController;
    Film film;
    Film film2;
    Film filmEmptyName;
    Film filmLongDescription;
    Film filmTooOld;
    Film filmNegativeDuration;


    @BeforeEach
    public void TestFilms() {

        filmController = new FilmController(
                new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));

        film  = new Film(1, "Фильм"
                , "Описание"
                , 120
                , LocalDate.of(1999,12,1));

        film2  = new Film(2, "Фильм2"
                , "Описание"
                , 120
                , LocalDate.of(1999,12,1));

        filmEmptyName  = new Film(1, ""
                , "Описание"
                , 120
                , LocalDate.of(1999,12,1));

        filmLongDescription  = new Film(1, "Фильм"
                , "Посмотрев 20 полнометражных фильмов " +
                "о приключениях Джеймса Бонда мы поняли одно: " +
                "у Джеймса Бонда есть всего две настоящих страсти " +
                "- к своей работе, и к старушке Англии. Сколько " +
                "бы невероятных поклонниц не встречалось на его пути, " +
                "он всегда ставит на первый план свою работу и долг, " +
                "а только потом позволяет себе немного расслабиться и " +
                "провести время в объятиях очередной красавицы. " +
                "Однако, 21й фильм - 'Казино Рояль' с Дэниелом Крейгом " +
                "- изменил это неписаное правило и создатели франшизы " +
                "начали давать крен в сторону личной жизни великого " +
                "сыщика. Хорошо это или плохо, по одному фильму судить было не просто. " +
                "Поэтому авторы решили создать 'франшизу внутри франшизы' и все фильмы, " +
                "которые выходили после 'Казино', стали развивать тему личных отношений и " +
                "биографической истории персонажа в ущерб глобальным проектам агента Ноль Ноль Семь."
                , 120
                , LocalDate.of(1999,12,1));

        filmTooOld  = new Film(1, "Фильм"
                , "Описание"
                , 120
                , LocalDate.of(1888,12,1));

        filmNegativeDuration  = new Film(1, "Фильм"
                , "Описание"
                , -30
                , LocalDate.of(1999,12,1));

    }

    @Test
    public void AddUpdateFilm() {

        filmController.create(film);
        filmController.create(film2);
        assertEquals(2, filmController.findAll().size(), "Фильмы не добавляются");

        film2.setDescription("Описание2");
        filmController.update(film2);

        List<Film> films = new ArrayList<>(filmController.findAll());
        assertEquals("Описание2", films.get(1).getDescription(), "Фильмы не обновляются");

    }


    @Test
    public void EmptyNameTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        filmController.create(filmEmptyName);
                    }
                });

        assertEquals("Название не может быть пустым.", exception.getMessage());
    }

    @Test
    public void LongDescriptionTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        filmController.create(filmLongDescription);
                    }
                });

        assertEquals("Максимальная длина описание 200 символов.", exception.getMessage());
    }

    @Test
    public void TooOldReleaseTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        filmController.create(filmTooOld);
                    }
                });

        assertEquals("Выход фильма не может быть раньше 28.12.1895.", exception.getMessage());
    }

    @Test
    public void NegativeDurationTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        filmController.create(filmNegativeDuration);
                    }
                });

        assertEquals("Продолжительность фильма должна быть положительной.", exception.getMessage());
    }


}