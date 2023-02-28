package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int generateId;

    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        log.debug(user.toString());
        checkUserFields(user);

        user.setId(++generateId);
        users.put(user.getId(), user);
        log.debug(user.toString());
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        log.debug(user.toString());
        int id = user.getId();
        if (users.containsKey(id)) {
            checkUserFields(user);

            User existUser = users.get(id);
            existUser.setEmail(user.getEmail());
            existUser.setLogin(user.getLogin());
            existUser.setName(user.getName());
            existUser.setBirthday(user.getBirthday());

            log.debug(user.toString());
            return existUser;
        }

        throw new ValidationException("User с таким id не найден");
    }

    private void checkUserFields(User user) {
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
    }
}