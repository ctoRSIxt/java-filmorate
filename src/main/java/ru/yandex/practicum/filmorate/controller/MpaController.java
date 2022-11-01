package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.GenreUnknownException;
import ru.yandex.practicum.filmorate.exception.MpaUnknownException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;


@Slf4j
@RestController("/mpa")
public class MpaController {

        private MpaService mpaService;

        @Autowired
        public MpaController(MpaService mpaService) {
            this.mpaService = mpaService;
        }

        @GetMapping(value = "/mpa")
        public Collection<Mpa> findAll() {
            return mpaService.findAll();
        }

        @GetMapping(value = "/mpa/{mpaId}")
        public Mpa findById(@PathVariable long mpaId) {
            return mpaService.findById(mpaId).orElseThrow(
                    () -> new MpaUnknownException("No mpa with id =" + mpaId));
        }

}
