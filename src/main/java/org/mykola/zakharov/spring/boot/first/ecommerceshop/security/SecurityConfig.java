package org.mykola.zakharov.spring.boot.first.ecommerceshop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private HibernateWebAuthProvider authProvider;

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private SavedReqAwareAuthSuccessHandler savedReqAwareAuthSuccessHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .exceptionHandling()
            .authenticationEntryPoint(restAuthenticationEntryPoint)
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, "/api/auth/user/**").permitAll()
            .antMatchers(HttpMethod.POST, "/api/auth/user/**").permitAll()
            .antMatchers(HttpMethod.DELETE, "/api/auth/user/**").authenticated()
            .antMatchers(HttpMethod.GET, "/api/auth/role/**").permitAll()
            .antMatchers(HttpMethod.GET, "/api/categories/**").permitAll()
            .antMatchers(HttpMethod.GET, "/api/products/**").permitAll()
            .antMatchers("/api/cart/**").authenticated()
            .antMatchers("/shared/**").permitAll()
            .antMatchers("/admin/**").hasRole("ADMIN")
            .antMatchers("/api/**/admin/**").hasRole("ADMIN")
            .and()
            .formLogin()
            .successHandler(savedReqAwareAuthSuccessHandler)
            .failureUrl("/api/auth/user/onerror")
            .and()
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/api/auth/user/signedout");

        // Auth demo
        // 1. /login (POST)
        //username=user2&password=2 - admin
        //username=user1&password=1 - user
        // 2. /shared/pages/testpublic.html (GET)
        // 3. /admin/pages/testadmin.html (GET)
        // 4. /api/user (GET)
        // 5. /api/role (GET)
        // 6. /logout (GET)
    }
}
