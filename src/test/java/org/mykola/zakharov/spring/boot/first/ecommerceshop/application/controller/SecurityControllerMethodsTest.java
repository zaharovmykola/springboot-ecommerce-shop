package org.mykola.zakharov.spring.boot.first.ecommerceshop.application.controller;

import org.junit.jupiter.api.Test;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.controller.AuthController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;


import static org.junit.jupiter.api.Assertions.*;

/*
 * Класс тестов приложения для проверки рест-контроллера безопасности
 * прямыми вызовами его методов действий на серверной стороне
 * с полноценным доступом только к слою контроллеров
 * (другие составляющие приложения при необходимости нужно получать
 * в виде макетов, например, используя внедрение макетов аннотациями MockBean)
 * */

// включение режима теста приложения без запуска на реальном веб-сервере,
// без доступа к контексту приложения
// и к реализациям составляющих приложения вне слоя контроллеров
@SpringBootTest
public class SecurityControllerMethodsTest {

    // Обычное внедрение зависимости,
    // потому что в тестах приложения доступен функционал Спринг
    @Autowired
    public AuthController authController;

    @Test
    public void shouldThrowAuthenticationCredentialsNotFoundException() {
        // проверить, что вызов метода getAllRoles из объекта контроллера
        // выбросит исключение, потому что выполняется
        // неаутентифицированным пользователем
        assertThrows(AuthenticationCredentialsNotFoundException.class, () -> {
            authController.getAllRoles();
        });
    }

    // проверка возможности получения стандартной модели данных о пользователе
    // по его имени из пользовательского бина hibernateWebAuthProvider,
    // из стандартного метода loadUserByUsername
    @Test
    @WithUserDetails(
            value = "admin",
            userDetailsServiceBeanName = "hibernateWebAuthProvider")
    public void withUserDetailsTest() {}

    // успешный вызов защищенного метода пользователем
    // с именем и ролью Админ
    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN" })
    public void getAllRolesByAdminUserTest() {
        ResponseEntity responseEntity = authController.getAllRoles();
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    // успешный вызов защищенного метода пользователем
    // только с явно указанной ролью Админ
    @Test
    @WithMockUser(roles = { "ADMIN" })
    public void getAllRolesByAdminRoleTest() {
        ResponseEntity responseEntity = authController.getAllRoles();
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    // дополнительные тесты
    @Test
    @WithMockUser(username = "user")
    public void failGettingAllRolesByUser() {
        assertThrows(AccessDeniedException.class, () -> {
            ResponseEntity responseEntity = authController.getAllRoles();
            assertNotNull(responseEntity);
            assertEquals(responseEntity.getStatusCode(), HttpStatus.FORBIDDEN);
        });
    }

    @Test
    @WithMockUser(username = "admin", roles = { "ADMIN2" })
    public void failGettingAllRolesByAdmin() {
        assertThrows(AccessDeniedException.class, () -> {
            ResponseEntity responseEntity = authController.getAllRoles();
            assertNotNull(responseEntity);
            assertEquals(responseEntity.getStatusCode(), HttpStatus.FORBIDDEN);
        });
    }

}
