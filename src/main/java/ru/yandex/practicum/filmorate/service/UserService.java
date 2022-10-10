package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService {

    private UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public Collection<User> findAll() {
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

        user.getFriendsId().add(userToAdd.getId());
        userToAdd.getFriendsId().add(user.getId());

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

        user.getFriendsId().remove(userToRemove.getId());
        userToRemove.getFriendsId().remove(user.getId());
        return user;
    }


    public List<User> getAllFriends(long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new UserUnknownException("No user with id =" + id);
        }

        List<User> friends = new ArrayList<>();
        for (long friendId : user.getFriendsId()) {
            friends.add(userStorage.getUserById(friendId));
        };
        return friends;
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

        List<User> commonFriends = new ArrayList<>();

        HashSet<Long> intersection = new HashSet<>(user.getFriendsId());
        intersection.retainAll(otherUser.getFriendsId());

        for (long friendId : intersection) {
            commonFriends.add(userStorage.getUserById(friendId));
        };
        return commonFriends;
    }


}
