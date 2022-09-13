package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    private final int id;
    private final String name;
    private final String description;
    private final Duration duration;
    private final LocalDate releaseDate;
}
