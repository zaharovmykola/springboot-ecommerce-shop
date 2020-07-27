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

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = EcommerceShopApplication.class
)
@AutoConfigureMockMvc
@ContextConfiguration(classes = {
        HibernateWebAuthProvider.class,
        RestAuthenticationEntryPoint.class,
        SavedReqAwareAuthSuccessHandler.class,
        SecurityConfig.class
})
@AutoConfigureTestDatabase(replace = NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SecurityControllerRequestsTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestRestTemplate testRestTemplate;

    final String baseUrl = "http://localhost:" + 8090 + "/simplespa/";

    @Test
    @Order(1)
    public void performLoginDefault() throws Exception {
        mvc.perform(formLogin())
                .andExpect((redirectedUrl("/api/auth/user/onerror")));
    }

    @Test
    @Order(2)
    public void performLoginWithAdminUserPassword() throws Exception {
        mvc.perform(formLogin("/login")
                .user("admin")
                .password("AdminPassword1"))
                .andExpect((status().isOk()));
    }

    @Test
    @Order(3)
    public void performLoginWithParameterSet() throws Exception {
        mvc.perform(formLogin("/login")
                .user("username", "admin")
                .password("password", "WrongAdminPassword1"))
                .andExpect((redirectedUrl("/api/auth/user/onerror")));
    }

    @Test
    @Order(4)
    public void performLogout() throws Exception {
        mvc.perform(get("/logout"))
                .andExpect((redirectedUrl("/api/auth/user/signedout")));
    }

    @Test
    @Order(5)
    public void whenAdminRequestsAllRoles_ThenSuccess() throws Exception {
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
        ResponseEntity<String> response =
                testRestTemplate.getForEntity(baseUrl, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Order(8)
    public void whenAnonymousUserRequestsDeleteUser_ThenUnauthorized() {
        ResponseEntity<String> httpStatus =
                testRestTemplate.exchange(
                        baseUrl + "api/auth/user/2",
                        HttpMethod.DELETE,
                        new HttpEntity<>(anonymousUser()),
                        String.class);
        assertEquals(HttpStatus.UNAUTHORIZED, httpStatus.getStatusCode());
    }

    @Test
    @Order(9)
    public void whenLoggedUserRequestsDeleteUser_ThenSuccess() {
        ResponseEntity<String> httpStatus =
                testRestTemplate.exchange(
                        baseUrl + "api/auth/user/2",
                        HttpMethod.DELETE,
                        new HttpEntity<>(loginUser()),
                        String.class);
        assertEquals(HttpStatus.NO_CONTENT, httpStatus.getStatusCode());
    }

    private HttpHeaders login(String username, String password) {
        MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
        request.set("username", username);
        request.set("password", password);
        ResponseEntity<String> response =
                this.testRestTemplate.withBasicAuth(username, password)
                        .postForEntity("/login", request, String.class);
        HttpHeaders headers = response.getHeaders();
        String cookie = headers.getFirst(HttpHeaders.SET_COOKIE);
        HttpHeaders requestHttpHeaders = new HttpHeaders();
        requestHttpHeaders.set("Cookie", cookie);
        return requestHttpHeaders;
    }

    private HttpHeaders loginAdmin() {
        return login("admin", "AdminPassword1");
    }

    private HttpHeaders loginUser() {
        return login("one", "UserPassword1");
    }

    private HttpHeaders anonymousUser() {
        return new HttpHeaders();
    }
}