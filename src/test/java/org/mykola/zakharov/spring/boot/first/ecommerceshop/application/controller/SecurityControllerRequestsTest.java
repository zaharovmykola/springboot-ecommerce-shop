//package org.mykola.zakharov.spring.boot.first.ecommerceshop.application.controller;
//
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mykola.zakharov.spring.boot.first.ecommerceshop.controller.AuthController;
//import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.CategoryHibernateDAO;
//import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.ProductHibernateDAO;
//import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.RoleHibernateDAO;
//import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.UserHibernateDAO;
//import org.mykola.zakharov.spring.boot.first.ecommerceshop.security.HibernateWebAuthProvider;
//import org.mykola.zakharov.spring.boot.first.ecommerceshop.security.RestAuthenticationEntryPoint;
//import org.mykola.zakharov.spring.boot.first.ecommerceshop.security.SavedReqAwareAuthSuccessHandler;
//import org.mykola.zakharov.spring.boot.first.ecommerceshop.service.AuthService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(value = AuthController.class)
//@Import({HibernateWebAuthProvider.class, RestAuthenticationEntryPoint.class, SavedReqAwareAuthSuccessHandler.class, SecurityConfig.class})
//public class SecurityControllerRequestsTest {
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private PasswordEncoder passwordEncoder;
//
//    @MockBean
//    private AuthService authService;
//
//    @MockBean
//    private UserHibernateDAO userHibernateDAO;
//
//    @MockBean
//    private RoleHibernateDAO roleHibernateDAO;
//
//    @MockBean
//    private CategoryHibernateDAO categoryHibernateDAO;
//
//    @MockBean
//    private ProductHibernateDAO productHibernateDAO;
//
//    @Test
//    public void performLoginDefault() throws Exception {
//        mvc.perform(formLogin())
//                .andExpect((redirectedUrl("/api/auth/user/onerror")));
//    }
//
//    @Test
//    public void performLoginWithAdminUserPassword() throws Exception {
//        mvc.perform(formLogin("/login")
//                .user("admin")
//                .password("AdminPassword1"))
//                .andExpect((status().isOk()));
//    }
//
//    @Test
//    public void performLoginWithParameterSet() throws Exception {
//        mvc.perform(formLogin("/login")
//                .user("username", "admin")
//                .password("password", "WrongAdminPassword1"))
//                .andExpect((redirectedUrl("/api/auth/user/onerror")));
//    }
//
//    @Test
//    public void performLogout() throws Exception {
//        mvc.perform(logout("/logout"))
//                .andExpect((redirectedUrl("/api/auth/user/signedout")));
//    }
//
//    @Test
//    public void getAllRolesByAdminUserTest() throws Exception {
//        mvc.perform(get("/api/auth/admin/roles")
//                .with(httpBasic("admin","AdminPassword1")))
//                .andExpect((status().isOk()));
//    }
//}
