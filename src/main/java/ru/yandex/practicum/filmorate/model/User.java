package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
}
