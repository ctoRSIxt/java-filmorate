package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Film {
    private long id;
    private String title;
    private String description;
    private int duration;
    private LocalDate releaseDate;
    private String rating;
    private List<String> genre = new ArrayList<>();
    private Set<Long> likes = new HashSet<>();

    public Film(long id, String title, String description
            , int duration, LocalDate releaseDate, String rating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.rating = rating;
    }
}
