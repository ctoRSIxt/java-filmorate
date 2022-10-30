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
    private String name;
    private String description;
    private int duration;
    private LocalDate releaseDate;
    private List<Genre> genre = new ArrayList<>();
    private Mpa mpa;
    private Set<Long> likes = new HashSet<>();


//    public Film(long id, String name, String description, int duration
//            , LocalDate releaseDate) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.duration = duration;
//        this.releaseDate = releaseDate;
//    }


    public Film(long id, String name, String description, int duration
            , LocalDate releaseDate, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.mpa = mpa;
//        this.genre = genre;
//        this.mpa = mpa;
//        this.likes = likes;
    }
}
