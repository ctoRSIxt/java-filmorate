package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User findById(long userId) {

        User user = userStorage.findUserById(userId);
        if (user == null) {
            throw new UserUnknownException("No user with id =" + userId);
        }

        return user;
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User addFriend(long id, long friendId) {

        User user = userStorage.findUserById(id);
        if (user == null) {
            throw new UserUnknownException("No user with id =" + id);
        }
        User userToAdd = userStorage.findUserById(friendId);
        if (userToAdd == null) {
            throw new UserUnknownException("No user with id =" + friendId);
        }

        user.getFriends().put(userToAdd.getId(), false);
        userStorage.update(user);

        return user;
    }

    public User removeFromFriends(long id, long friendId) {

        User user = userStorage.findUserById(id);
        if (user == null) {
            throw new UserUnknownException("No user with id =" + id);
        }
        User userToRemove = userStorage.findUserById(friendId);
        if (userToRemove == null) {
            throw new UserUnknownException("No user with id =" + friendId);
        }

        user.getFriends().remove(userToRemove.getId());
        userStorage.update(user);
        userToRemove.getFriends().remove(user.getId());
        userStorage.update(user);
        return user;
    }

    public List<User> getAllFriends(long id) {
        User user = userStorage.findUserById(id);
        if (user == null) {
            throw new UserUnknownException("No user with id =" + id);
        }

        return user.getFriends().keySet().stream()
                .map(friendId -> userStorage.findUserById(friendId))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long id, long otherId) {

        User user = userStorage.findUserById(id);
        if (user == null) {
            throw new UserUnknownException("No user with id =" + id);
        }

        User otherUser = userStorage.findUserById(otherId);
        if (otherUser == null) {
            throw new UserUnknownException("No user with id =" + otherId);
        }

        HashSet<Long> intersection = new HashSet<>(user.getFriends().keySet());
        intersection.retainAll(otherUser.getFriends().keySet());
        return intersection.stream()
                .map(friendId -> userStorage.findUserById(friendId))
                .collect(Collectors.toList());
    }
}
