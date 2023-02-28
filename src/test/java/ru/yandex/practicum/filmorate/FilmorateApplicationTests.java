package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest()
@AutoConfigureMockMvc
class FilmorateApplicationTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	// ---------------------------------ТЕСТЫ ФИЛЬМОВ------------------------------------------------------
	// ----------------------------Валидные данные фильма, метод Post--------------------------------------
	@SneakyThrows
	@Test
	void normalPostFilm_Status_200() {
		Film film = Film.builder()
				.name("Test Film")
				.description("About Test Film")
				.releaseDate(LocalDate.of(2000, 1, 1))
				.duration(100)
				.build();

		mockMvc.perform(post("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(film)))
				.andExpect(status().is(200));
	}

	// ----------------------------Пустое имя фильма, метод Post---------------------------------------------
	@SneakyThrows
	@Test
	void emptyNameFilm_Status_500() {
		Film film = Film.builder()
				.name("")
				.description("About Test Film")
				.releaseDate(LocalDate.of(2000, 1, 1))
				.duration(100)
				.build();

		mockMvc.perform(post("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(film)))
				.andExpect(status().is(500));
	}

	// ----------------------------Описание фильма больше 200 символов, метод Post------------------------------
	@SneakyThrows
	@Test
	void longDescriptionFilm_Status_500() {
		Film film = Film.builder()
				.name("Test Film")
				.description("About Test Film About Test Film About Test Film About Test Film" +
						"About Test Film About Test Film About Test Film About Test Film " +
						"About Test Film About Test Film About Test Film About Test Film About Test Film")
				.releaseDate(LocalDate.of(2000, 1, 1))
				.duration(100)
				.build();

		mockMvc.perform(post("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(film)))
				.andExpect(status().is(500));
	}

	// ----------------------------Некорректная дата фильма, метод Post------------------------------------------
	@SneakyThrows
	@Test
	void wrongDateFilm_Status_500() {
		Film film = Film.builder()
				.name("Test Film")
				.description("About Test Film")
				.releaseDate(LocalDate.of(1850, 1, 1))
				.duration(100)
				.build();

		mockMvc.perform(post("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(film)))
				.andExpect(status().is(500));
	}

	// ----------------------------Продолжительность фильма должна быть больше нуля, метод Post-----------------
	@SneakyThrows
	@Test
	void negativeDurationFilm_Status_500() {
		Film film = Film.builder()
				.name("Test Film")
				.description("About Test Film")
				.releaseDate(LocalDate.of(2000, 1, 1))
				.duration(0)
				.build();

		mockMvc.perform(post("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(film)))
				.andExpect(status().is(500));
	}

	// ----------------------------Валидные данные фильма, метод Put------------------------------------------
	@SneakyThrows
	@Test
	void normalPutFilm_Status_200() {
		Film film = Film.builder()
				.name("Test Film")
				.description("About Test Film")
				.releaseDate(LocalDate.of(2000, 1, 1))
				.duration(100)
				.build();

		mockMvc.perform(post("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(film)))
				.andExpect(status().is(200));

		Film updateFilm = Film.builder()
				.id(1)
				.name("Updated Film name")
				.description("Updated Film description")
				.releaseDate(LocalDate.of(2000, 1, 1))
				.duration(100)
				.build();

		mockMvc.perform(put("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(updateFilm)))
				.andExpect(status().is(200));
	}

	// ----------------------------Некорректный id фильма, метод Put------------------------------------------
	@SneakyThrows
	@Test
	void wrongIdFilm_Status_500() {
		Film film = Film.builder()
				.name("Test Film")
				.description("About Test Film")
				.releaseDate(LocalDate.of(2000, 1, 1))
				.duration(100)
				.build();

		mockMvc.perform(post("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(film)))
				.andExpect(status().is(200));

		Film updateFilm = Film.builder()
				.id(999)
				.name("Updated Film name")
				.description("Updated Film description")
				.releaseDate(LocalDate.of(2000, 1, 1))
				.duration(100)
				.build();

		mockMvc.perform(put("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(updateFilm)))
				.andExpect(status().is(500));
	}

	// ----------------------------Получение всех фильмов, метод Get---------------------------------------
	@SneakyThrows
	@Test
	void normalGetFilm_Status_200() {
		Film film = Film.builder()
				.name("Test Film")
				.description("About Test Film")
				.releaseDate(LocalDate.of(2000, 1, 1))
				.duration(100)
				.build();

		mockMvc.perform(post("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(film)))
				.andExpect(status().is(200));

		Film film2 = Film.builder()
				.name("Test Film 2")
				.description("About Test Film 2")
				.releaseDate(LocalDate.of(1940, 7, 7))
				.duration(100)
				.build();

		mockMvc.perform(post("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(film2)))
				.andExpect(status().is(200));

		Film film3 = Film.builder()
				.name("Test Film 3")
				.description("About Test Film 3")
				.releaseDate(LocalDate.of(2010, 5, 5))
				.duration(100)
				.build();

		mockMvc.perform(post("/films")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(film3)))
				.andExpect(status().is(200));

		mockMvc.perform(get("/films")).andExpect(status().is(200));
	}

	// ---------------------------------ТЕСТЫ ПОЛЬЗОВАТЕЛЕЙ------------------------------------------------
	// ----------------------------Валидные данные пользователя, метод Post--------------------------------
	@SneakyThrows
	@Test
	void normalPostUser_Status_200() {
		User user = User.builder()
				.email("mail@mail.ru")
				.login("TestLoginUser")
				.name("Test Name User")
				.birthday(LocalDate.of(2000, 1, 1))
				.build();

		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().is(200));
	}

	// ----------------------------Пустая почта пользователя, метод Post-------------------------------------
	@SneakyThrows
	@Test
	void emptyEmailUser_Status_500() {
		User user = User.builder()
				.email("")
				.login("TestLoginUser")
				.name("Test Name User")
				.birthday(LocalDate.of(2000, 1, 1))
				.build();

		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().is(500));
	}

	// ----------------------------Почта без символа @ у пользователя, метод Post------------------------------
	@SneakyThrows
	@Test
	void wrongEmailUser_Status_500() {
		User user = User.builder()
				.email("mail.ru")
				.login("TestLoginUser")
				.name("Test Name User")
				.birthday(LocalDate.of(2000, 1, 1))
				.build();

		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().is(500));
	}

	// ----------------------------Пустой логин пользователя, метод Post---------------------------------------
	@SneakyThrows
	@Test
	void emptyLoginUser_Status_500() {
		User user = User.builder()
				.email("mail@mail.ru")
				.login("")
				.name("Test Name User")
				.birthday(LocalDate.of(2000, 1, 1))
				.build();

		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().is(500));
	}

	// ----------------------------Логин с пробелами у пользователя, метод Post---------------------------------
	@SneakyThrows
	@Test
	void wrongLoginUser_Status_500() {
		User user = User.builder()
				.email("mail@mail.ru")
				.login("Test Login User")
				.name("Test Name User")
				.birthday(LocalDate.of(2000, 1, 1))
				.build();

		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().is(500));
	}

	// ----------------------------Некорректная дата рождения пользователя, метод Post----------------------------
	@SneakyThrows
	@Test
	void wrongBirthdayUser_Status_500() {
		User user = User.builder()
				.email("mail@mail.ru")
				.login("TestLoginUser")
				.name("Test Name User")
				.birthday(LocalDate.of(2030, 1, 1))
				.build();

		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().is(500));
	}

	// ----------------------------Валидные данные пользователя, метод Put--------------------------------
	@SneakyThrows
	@Test
	void normalPutUser_Status_200() {
		User user = User.builder()
				.email("mail@mail.ru")
				.login("TestLoginUser")
				.name("Test Name User")
				.birthday(LocalDate.of(2000, 1, 1))
				.build();

		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().is(200));

		User updateUser = User.builder()
				.id(1)
				.email("mail@yandex.ru")
				.login("TestLoginUser")
				.name("Updated Name User")
				.birthday(LocalDate.of(2001, 1, 1))
				.build();

		mockMvc.perform(put("/users")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(updateUser)))
				.andExpect(status().is(200));
	}

	// ----------------------------Некорректный id пользователя, метод Put--------------------------------
	@SneakyThrows
	@Test
	void wrongIdUser_Status_500() {
		User user = User.builder()
				.email("mail@mail.ru")
				.login("TestLoginUser")
				.name("Test Name User")
				.birthday(LocalDate.of(2000, 1, 1))
				.build();

		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().is(200));

		User updateUser = User.builder()
				.id(100)
				.email("mail@yandex.ru")
				.login("TestLoginUser")
				.name("Updated Name User")
				.birthday(LocalDate.of(2001, 1, 1))
				.build();

		mockMvc.perform(put("/users")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(updateUser)))
				.andExpect(status().is(500));
	}

	// ----------------------------Получение всех пользователей, метод Get--------------------------------
	@SneakyThrows
	@Test
	void normalGetUser_Status_200() {
		User user = User.builder()
				.email("mail@mail.ru")
				.login("TestLoginUser")
				.name("Test Name User")
				.birthday(LocalDate.of(2000, 1, 1))
				.build();

		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().is(200));

		User user2 = User.builder()
				.email("mail@yandex.ru")
				.login("TestLoginUser2")
				.name("Test Name User 2")
				.birthday(LocalDate.of(2000, 1, 1))
				.build();

		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(user2)))
				.andExpect(status().is(200));

		User user3 = User.builder()
				.email("mail@google.com")
				.login("TestLoginUser3")
				.name("Test Name User 3")
				.birthday(LocalDate.of(2000, 1, 1))
				.build();

		mockMvc.perform(post("/users")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(user3)))
				.andExpect(status().is(200));

		mockMvc.perform(get("/users")).andExpect(status().is(200));
	}
}