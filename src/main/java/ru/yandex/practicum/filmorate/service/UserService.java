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

        User user = userStorage.getUserById(userId);
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

        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new UserUnknownException("No user with id =" + id);
        }
        User userToAdd = userStorage.getUserById(friendId);
        if (userToAdd == null) {
            throw new UserUnknownException("No user with id =" + friendId);
        }

        user.getFriends().put(userToAdd.getId(), false);

        return user;
    }

    public User removeFromFriends(long id, long friendId) {

        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new UserUnknownException("No user with id =" + id);
        }
        User userToRemove = userStorage.getUserById(friendId);
        if (userToRemove == null) {
            throw new UserUnknownException("No user with id =" + friendId);
        }

        user.getFriends().remove(userToRemove.getId());
        userToRemove.getFriends().remove(user.getId());
        return user;
    }

    public List<User> getAllFriends(long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new UserUnknownException("No user with id =" + id);
        }

        return user.getFriends().keySet().stream()
                .map(friendId -> userStorage.getUserById(friendId))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long id, long otherId) {

        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new UserUnknownException("No user with id =" + id);
        }

        User otherUser = userStorage.getUserById(otherId);
        if (otherUser == null) {
            throw new UserUnknownException("No user with id =" + otherId);
        }

        HashSet<Long> intersection = new HashSet<>(user.getFriends().keySet());
        intersection.retainAll(otherUser.getFriends().keySet());
        return intersection.stream()
                .map(friendId -> userStorage.getUserById(friendId))
                .collect(Collectors.toList());
    }
}
