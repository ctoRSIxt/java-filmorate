package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserUnknownException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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
        return userStorage.findUserById(userId);
    }

    public User create(User user) {
        validateUser(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        validateUser(user);
        return userStorage.update(user);
    }

    public User addFriend(long id, long friendId) {
        User user = userStorage.findUserById(id);
        User userToAdd = userStorage.findUserById(friendId);

        user.getFriends().put(userToAdd.getId(), false);
        userStorage.update(user);

        return user;
    }

    public User removeFromFriends(long id, long friendId) {
        User user = userStorage.findUserById(id);
        User userToRemove = userStorage.findUserById(friendId);

        user.getFriends().remove(userToRemove.getId());
        userStorage.update(user);
        userToRemove.getFriends().remove(user.getId());
        userStorage.update(user);
        return user;
    }

    public List<User> getAllFriends(long id) {
        User user = userStorage.findUserById(id);

        return user.getFriends().keySet().stream()
                .map(friendId -> userStorage.findUserById(friendId))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(long id, long otherId) {

        User user = userStorage.findUserById(id);
        User otherUser = userStorage.findUserById(otherId);

        HashSet<Long> intersection = new HashSet<>(user.getFriends().keySet());
        intersection.retainAll(otherUser.getFriends().keySet());
        return intersection.stream()
                .map(friendId -> userStorage.findUserById(friendId))
                .collect(Collectors.toList());
    }


    private void validateUser(User user) {

        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.info("User: Валидация не пройдена: email пуст или не содержит @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать @");
        }

        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.info("User: Валидация не пройдена: логин пуст или содержит пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }

        if (user.getName() == null || user.getName().isBlank()) {
            log.info("User: Имя пользователя пустое: будет использоваться логин");
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("User: Валидация не пройдена: дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
