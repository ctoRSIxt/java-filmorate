package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;

@Data
@AllArgsConstructor
public class Film {
    private long id;
    private String name;
    private String description;
    private int duration;
    private LocalDate releaseDate;
    private HashSet<Long> likes;
}
