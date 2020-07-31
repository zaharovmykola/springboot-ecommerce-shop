package org.mykola.zakharov.spring.boot.first.ecommerceshop.application.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.EcommerceShopApplication;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.security.HibernateWebAuthProvider;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.security.RestAuthenticationEntryPoint;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.security.SavedReqAwareAuthSuccessHandler;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * Класс тестов приложения для проверки системы безопасности
 * веб-запросами с тестовых клиентов к полноценному веб-приложению,
 * запущенному на реальном сервере
 * */

// интеграция Spring TestContext с JUnit 5 Jupiter Test
@ExtendWith(SpringExtension.class)
// включение режима теста приложения с запуском на реальном веб-сервере
// и с доступом к контексту приложения
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = EcommerceShopApplication.class
)
// активация контейнера для внедрения простого тестового веб-клиента MockMvc
@AutoConfigureMockMvc
// подключение пользовательских конфигурационных файлов системы безопасности
@ContextConfiguration(classes = {
        HibernateWebAuthProvider.class,
        RestAuthenticationEntryPoint.class,
        SavedReqAwareAuthSuccessHandler.class,
        SecurityConfig.class
})
// отмена замещения реальной БД
@AutoConfigureTestDatabase(replace = NONE)
// режим создания одиночного экземпляра класса тестов для всех кейсов
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// активация контейнера выполнения кейсов согласно пользовательской нумерации
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SecurityControllerRequestsTest {

    @Autowired
    private MockMvc mvc; // Простой веб-клиент для тестов

    @Autowired
    private TestRestTemplate testRestTemplate; // Сложный веб-клиент для гибких тестов

    final String baseUrl = "http://localhost:" + 8090 + "/eCommerceShop/";

    @Test
    @Order(1) // Этот кейс выполнить первым
    public void performLoginDefault() throws Exception {
        // отправить стандартный пост-запрос для входа
        mvc.perform(formLogin("/login"))
                // и ожидать в ответ перенаправления на адрес действия рест-контроллера безопасности,
                .andExpect((redirectedUrl("/api/auth/user/onerror")));
    }

    @Test
    @Order(2) // Этот кейс выполнить вторым
    public void performLoginWithAdminUserPassword() throws Exception {
        // отправить стандартный пост-запрос для входа
        // (имя конечной точки по умолчанию)
        mvc.perform(formLogin("/login")
                .user("admin") // в теле запроса отправить имя
                .password("AdminPassword1")) // в теле запроса отправить пароль
                .andExpect((status().isOk())); // и ожидать статус-код 200 (OK)
    }

    @Test
    @Order(3)
    public void performLoginWithWrongUserPassword() throws Exception {
        mvc.perform(formLogin("/login")
                .user("username", "admin")
                // * "WrongAdminPassword1" is a wrong password
                .password("password", "WrongAdminPassword1"))
                .andExpect((redirectedUrl("/api/auth/user/onerror")));
    }

    @Test
    @Order(4)
    public void performLogout() throws Exception {
        // отправка get-запроса на конечную точку выхода из аккаунта
        // (указана в файле SecurityConfig)
        mvc.perform(get("/logout"))
                .andExpect((redirectedUrl("/api/auth/user/signedout")));
    }

    @Test
    @Order(5)
    public void whenAdminRequestsAllRoles_ThenSuccess() throws Exception {
        // получить список всех ролей http-запросом GET,
        // войдя предварительно как администратор (loginAdmin())
        ResponseEntity<String> response =
                testRestTemplate.exchange(
                        baseUrl + "api/auth/admin/roles",
                        HttpMethod.GET,
                        new HttpEntity<String>(loginAdmin()),
                        String.class
                );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(6)
    public void whenLoggedUserRequestsAllRoles_ThenForbidden() throws Exception {
        // получить ответ "воспрещено" на попытку
        // получить список всех ролей http-запросом GET,
        // войдя предварительно как простой зарегистрированный пользователь (loginUser())
        ResponseEntity<String> response =
                testRestTemplate.exchange(
                        baseUrl + "api/auth/admin/roles",
                        HttpMethod.GET,
                        new HttpEntity<String>(loginUser()),
                        String.class
                );
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @Order(7)
    public void whenAnyUserRequestsIndexPage_ThenSuccess() throws Exception {
        // гет-запрос без данных авторизации
        // с ожиданием тела ответа (базовая веб-страница)
        ResponseEntity<String> response =
                testRestTemplate.getForEntity(baseUrl, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(8) // Этот кейс выполнить вторым
    public void performLoginWithUserPassword() throws Exception {
        // отправить стандартный пост-запрос для входа
        // (имя конечной точки по умолчанию)
        mvc.perform(formLogin("/login")
                .user("one") // в теле запроса отправить имя
                .password("UserPassword1")) // в теле запроса отправить пароль
                .andExpect((status().isOk())); // и ожидать статус-код 200 (OK)
    }

    @Test
    @Order(9)
    public void whenAnonymousUserRequestsDeleteUser_ThenUnauthorized() {
        // получить ответ "не авторизован" на попытку
        // удалить пользователя с ИД 2 http-запросом DELETE,
        // войдя предварительно как незарегистрированный пользователь (anonymousUser())
        ResponseEntity<String> httpStatus =
                testRestTemplate.exchange(
                        baseUrl + "api/auth/user/2",
                        HttpMethod.DELETE,
                        new HttpEntity<>(anonymousUser()),
                        String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, httpStatus.getStatusCode());
    }

    @Test
    @Order(10)
    public void whenLoggedUserRequestsDeleteAnotherUser_ThenForbidden() {
        // получить отрицательный ответ "нет содержимого" на попытку
        // удалить пользователя с ИД 3 http-запросом DELETE,
        // войдя предварительно как простой зарегистрированный пользователь (loginUser())
        ResponseEntity<String> httpStatus =
                testRestTemplate.exchange(
                        baseUrl + "api/auth/user/3",
                        HttpMethod.DELETE,
                        new HttpEntity<>(loginUser()),
                        String.class);
        assertEquals(HttpStatus.FORBIDDEN, httpStatus.getStatusCode());
    }

    @Test
    @Order(11)
    public void whenLoggedAdminRequestsDeleteHimself_ThenSuccess() {
        // получить положительный ответ "нет содержимого" на попытку
        // удалить пользователя с ИД 1 http-запросом DELETE,
        // войдя предварительно как простой зарегистрированный пользователь (loginUser())
        ResponseEntity<String> httpStatus =
                testRestTemplate.exchange(
                        baseUrl + "api/auth/user/1",
                        HttpMethod.DELETE,
                        new HttpEntity<>(loginAdmin()),
                        String.class);
        assertEquals(HttpStatus.NO_CONTENT, httpStatus.getStatusCode());
    }

    @Test
    @Order(12)
    public void whenLoggedUserRequestsDeleteHimself_ThenSuccess() {
        // получить положительный ответ "нет содержимого" на попытку
        // удалить пользователя с ИД 2 http-запросом DELETE,
        // войдя предварительно как простой зарегистрированный пользователь (loginUser())
        ResponseEntity<String> httpStatus =
                testRestTemplate.exchange(
                        baseUrl + "api/auth/user/2",
                        HttpMethod.DELETE,
                        new HttpEntity<>(loginUser()),
                        String.class);
        assertEquals(HttpStatus.NO_CONTENT, httpStatus.getStatusCode());
    }

    // метод тестового входа в ааккаунт (не тест-кейс),
    // используется кейсами текущего класса теста,
    // в которых нужно проверять обращения по сети в веб-приложение
    // после входа
    private HttpHeaders login(String username, String password) {
        // Словарь данных учетной записи для входа
        MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
        request.set("username", username);
        request.set("password", password);
        // Сложным тестовым веб-клиентом делаем запрос
        // на стандартную кончную точку входа
        ResponseEntity<String> response =
                this.testRestTemplate.withBasicAuth(username, password)
                        .postForEntity("/login", request, String.class);
        // Из полученного ответа извлекаем заголовки
        HttpHeaders headers = response.getHeaders();
        // Из заголовков ответа извлекаем значение заголовка,
        // устанавливающего веб-клиенту сессионный куки
        String cookie = headers.getFirst(HttpHeaders.SET_COOKIE);
        // Включаем сессионный куки вошедшего пользователя
        // в загололовки для следующих запросов - в специальный заголовок
        // с именем Cookie
        HttpHeaders requestHttpHeaders = new HttpHeaders();
        requestHttpHeaders.set("Cookie", cookie);
        // возврат заголовков, настроенных как у веб-клиента
        // для выполнения последующих веб-запросов
        return requestHttpHeaders;
    }

    // получить заголовки для веб-запроса аутентифицированного пользователя-администратора
    private HttpHeaders loginAdmin() {
        return login("admin", "AdminPassword1");
    }

    // получить заголовки для веб-запроса аутентифицированного пользователя не-администратора
    private HttpHeaders loginUser() {
        return login("one", "UserPassword1");
    }

    // получить пустые заголовки для веб-запроса неаутентифицированного пользователя
    private HttpHeaders anonymousUser() {
        return new HttpHeaders();
    }
}