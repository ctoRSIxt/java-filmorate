package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateUserTests {
    private final UserDbStorage userStorage;

    User user1;
    User user2;

    @BeforeEach
    public void TestUsers() {
        user1 = new User(1, "some@email.com"
                , "login", "Movie Fan"
                , LocalDate.of(2000, 10, 1));

        user2 = new User(2, "some@email.com"
                , "login2", "Movie Fan2"
                , LocalDate.of(2000, 10, 1));

    }

    @Test
    public void testFindUserById() {
        userStorage.create(user1);
        User user = userStorage.findUserById(1);

        assertThat(user).hasFieldOrPropertyWithValue("id", 1L);

    }

    @Test
    public void testFindAllUsers() {
        userStorage.create(user1);
        userStorage.create(user2);

        List<User> users = userStorage.findAll();
        assertThat(users).hasSize(2);
    }

    @Test
    public void testUpdateUser() {
        userStorage.create(user1);
        User user = userStorage.findUserById(1);

        String newName = "Test Name";
        user.setName(newName);
        userStorage.update(user);
        User updateUser = userStorage.findUserById(1);
        assertThat(updateUser).hasFieldOrPropertyWithValue("name", newName);
    }
}