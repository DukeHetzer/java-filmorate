package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int generateId;

    @GetMapping("/films")
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        log.debug(film.toString());
        checkFilmFields(film);

        film.setId(++generateId);
        films.put(film.getId(), film);
        log.debug(film.toString());
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        log.debug(film.toString());
        int id = film.getId();
        if (films.containsKey(id)) {
            checkFilmFields(film);

            Film existFilm = films.get(id);
            existFilm.setName(film.getName());
            existFilm.setDescription(film.getDescription());
            existFilm.setReleaseDate(film.getReleaseDate());
            existFilm.setDuration(film.getDuration());

            log.debug(film.toString());
            return existFilm;
        }

        throw new ValidationException("Film с таким id не найден");
    }

    private void checkFilmFields(Film film) {
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
    }
}