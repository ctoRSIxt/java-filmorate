package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.Duration;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    private int id;
    private String name;
    private String description;
    private int duration;
    private LocalDate releaseDate;
}
