package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController("/users")
public class UserController {
    private UserStorage userStorage;
    private UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping(value = "/users")
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @GetMapping(value = "/users/{userId}")
    public User findById(@PathVariable long userId) {
        return userStorage.getUserById(userId);
    }

    @PostMapping(value = "/users")
    public User create(@RequestBody User user) {
        return userStorage.create(user);
    }

    @PutMapping(value = "/users")
    public User update(@RequestBody User user) {
        return userStorage.update(user);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable long friendId) {

        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new UserUnknownException("No user with id =" + id);
        }
        User userToAdd = userStorage.getUserById(friendId);
        if (userToAdd == null) {
            throw new UserUnknownException("No user with id =" + friendId);
        }

        userService.addToFriends(user,userToAdd);
        return user;
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public User removeFromFriends(@PathVariable long id, @PathVariable long friendId) {

        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new UserUnknownException("No user with id =" + id);
        }
        User userToRemove = userStorage.getUserById(friendId);
        if (userToRemove == null) {
            throw new UserUnknownException("No user with id =" + friendId);
        }

        userService.removeFromFriends(user, userToRemove);
        return user;
    }

    @GetMapping(value = "/users/{id}/friends")
    public List<User> getAllFriends(@PathVariable long id) {

        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new UserUnknownException("No user with id =" + id);
        }

        List<User> friends = new ArrayList<>();
        for (long friendId : userService.getAllFriends(user)) {
            friends.add(userStorage.getUserById(friendId));
        };
        return friends;
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public List<User> getAllFriends(@PathVariable long id, @PathVariable long otherId) {

        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new UserUnknownException("No user with id =" + id);
        }

        User otherUser = userStorage.getUserById(id);
        if (otherUser == null) {
            throw new UserUnknownException("No user with id =" + id);
        }

        List<User> mutualFriends = new ArrayList<>();
        for (long friendId : userService.getMutualFriends(user, otherUser)) {
            mutualFriends.add(userStorage.getUserById(friendId));
        };
        return mutualFriends;
    }


}
