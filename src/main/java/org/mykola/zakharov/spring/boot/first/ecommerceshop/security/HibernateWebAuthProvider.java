package org.mykola.zakharov.spring.boot.first.ecommerceshop.security;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.UserHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class HibernateWebAuthProvider implements AuthenticationProvider, UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserHibernateDAO userDAO;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userDAO.findUserByName(userName);
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return AuthorityUtils.createAuthorityList(/*"ROLE_" +*/ user.getRole().getName());
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getName();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }

    @Override
    @Transactional
    public Authentication authenticate(Authentication a) throws AuthenticationException {

        String name = a.getName();
        String password = a.getCredentials().toString();
        User user = null;
        try {
            user = userDAO.findUserByName(name);
        } catch (Exception ex) {
            Logger.getLogger(HibernateWebAuthProvider.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (user != null) {
            System.out.println(user.getPassword());
            System.out.println(passwordEncoder.matches(password, user.getPassword()));
        }

        if (user != null
                && user.getRole() != null
                && passwordEncoder.matches(password, user.getPassword())
        ) {
            System.out.println(user.getPassword());
            System.out.println(password);
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(/*"ROLE_" +*/ user.getRole().getName()));
            return new UsernamePasswordAuthenticationToken(name, password, authorities);
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(UsernamePasswordAuthenticationToken.class);
    }
}
