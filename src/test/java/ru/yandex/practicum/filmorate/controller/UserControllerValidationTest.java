package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerValidationTest {

    UserController userController;
    User user;
    User user2;
    User userEmptyEmail;
    User userNoAtEmail;
    User userLoginWithSpace;
    User userEmptyName;
    User userTooYoung;


    @BeforeEach
    public void TestUsers() {

        userController = new UserController();

        user = new User(1, "some@email.com"
                , "login", "Movie Fan"
                , LocalDate.of(2000,10,1));

        user2 = new User(2, "some@email.com"
                , "login2", "Movie Fan2"
                , LocalDate.of(2000,10,1));

        userEmptyEmail = new User(1, "  "
                , "login", "Movie Fan"
                , LocalDate.of(2000,10,1));

        userNoAtEmail = new User(1, "someemail.com"
                , "login", "Movie Fan"
                , LocalDate.of(2000,10,1));

        userLoginWithSpace = new User(1, "some@email.com"
                , "login login", "Movie Fan"
                , LocalDate.of(2000,10,1));

        userEmptyName = new User(1, "some@email.com"
                , "login", ""
                , LocalDate.of(2000,10,1));

        userTooYoung = new User(1, "some@email.com"
                , "login", ""
                , LocalDate.of(2030,10,1));
    }


    @Test
    public void AddUpdateFilm() {

        userController.create(user);
        userController.create(user2);
        assertEquals(2, userController.findAll().size(), "Пользователи не добавляются");

        user2.setLogin("LOGIN222");
        userController.put(user2);

        List<User> users = new ArrayList<>(userController.findAll());
        assertEquals("LOGIN222", users.get(1).getLogin(), "Пользователи не обновляются");

    }


    @Test
    public void EmptyEmailTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        userController.create(userEmptyEmail);
                    }
                });

        assertEquals("Электронная почта не " +
                "может быть пустой и должна содержать @", exception.getMessage());
    }

    @Test
    public void NoAtEmailTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        userController.create(userNoAtEmail);
                    }
                });

        assertEquals("Электронная почта не " +
                "может быть пустой и должна содержать @", exception.getMessage());
    }

    @Test
    public void LoginWithSpaceTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        userController.create(userLoginWithSpace);
                    }
                });

        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    public void EmptyNameTest() {

        assertEquals(userEmptyName.getLogin(), userController.create(userEmptyName).getName()
                , "Логин не присвоился как имя");
    }

    @Test
    public void TooYoungUserTest() {
        final ValidationException exception = assertThrows(
                ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() {
                        userController.create(userTooYoung);
                    }
                });

        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

}