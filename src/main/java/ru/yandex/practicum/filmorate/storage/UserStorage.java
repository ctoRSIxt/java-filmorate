package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    // Return list of all users
    public Collection<User> findAll();

    // Return user by id
    public User getUserById(long id);

    // Create a new user
    public User create(User user);

    // Update an existing user
    public User update(User user);

}
