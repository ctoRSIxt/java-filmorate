package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Service
public class UserService {


    public void addToFriends(User user, User userToAdd) {
        user.getFriendsId().add(userToAdd.getId());
        userToAdd.getFriendsId().add(user.getId());
    }

    public void removeFromFriends(User user, User userToRemove) {
        user.getFriendsId().remove(userToRemove.getId());
        userToRemove.getFriendsId().remove(user.getId());
    };

    public Collection<Long> getMutualFriends(User user1, User user2) {
        HashSet<Long> intersection = new HashSet<>(user1.getFriendsId());
        intersection.retainAll(user2.getFriendsId());
        return intersection;
    };

}
