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
    private Mpa mpa;
    private List<Genre> genres = new ArrayList<>();
    private Set<Long> likes = new HashSet<>();

    public Film(long id, String name, String description, int duration
            , LocalDate releaseDate, Mpa mpa, List<Genre> genres, Set<Long> likes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.mpa = mpa;
        this.genres = genres;
        this.likes = likes;
    }


//    public Film(long id, String name, String description, int duration
//            , LocalDate releaseDate) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.duration = duration;
//        this.releaseDate = releaseDate;
//    }


//    public Film(long id, String name, String description, int duration
//            , LocalDate releaseDate, Mpa mpa, List<Genre> genre, Set<Long> likes) {
//        this.id = id;
//        this.name = name;
//        this.description = description;
//        this.duration = duration;
//        this.releaseDate = releaseDate;
//        this.mpa = mpa;
////        this.genre = genre;
////        this.mpa = mpa;
////        this.likes = likes;
//    }
}
