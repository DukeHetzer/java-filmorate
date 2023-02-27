package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestController
public class FilmController {
    private final Set<Film> films = new HashSet<>();
    private int generateId;

    @GetMapping("/films")
    public Set<Film> getFilms() {
        return films;
    }

    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        log.debug(film.toString());

        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }

        film.setId(++generateId);
        films.add(film);
        log.debug(film.toString());
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        log.debug(film.toString());
        for (Film existFilm : films) {
            if (film.getId() == existFilm.getId()) {
                existFilm.setName(film.getName());
                existFilm.setDescription(film.getDescription());
                existFilm.setReleaseDate(film.getReleaseDate());
                existFilm.setDuration(film.getDuration());
                log.debug(film.toString());
                return existFilm;
            }
        }
        throw new ValidationException("Film с таким id не найден");
    }
}