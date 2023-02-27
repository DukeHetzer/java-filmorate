package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@RestController
public class UserController {
    private final Set<User> users = new HashSet<>();
    private int generateId;

    @GetMapping("/users")
    public Set<User> getUsers() {
        return users;
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        log.debug(user.toString());

        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Email не может быть пустым и должен содержать символ @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        user.setId(++generateId);
        users.add(user);
        log.debug(user.toString());
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        log.debug(user.toString());
        for (User existUser : users) {
            if (user.getId() == existUser.getId()) {
                existUser.setEmail(user.getEmail());
                existUser.setLogin(user.getLogin());
                existUser.setName(user.getName());
                existUser.setBirthday(user.getBirthday());
                log.debug(user.toString());
                return existUser;
            }
        }
        throw new ValidationException("User с таким id не найден");
    }
}