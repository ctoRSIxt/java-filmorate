package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    // Return list of all users
    public List<User> findAll();

    // Return user by id
    public User findUserById(long id);

    // Create a new user
    public User create(User user);

    // Update an existing user
    public User update(User user);

}
