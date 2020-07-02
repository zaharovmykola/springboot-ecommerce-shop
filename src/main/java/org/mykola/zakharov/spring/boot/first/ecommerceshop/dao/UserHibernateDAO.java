package org.mykola.zakharov.spring.boot.first.ecommerceshop.dao;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHibernateDAO extends JpaRepository<User, Long> {
    User findUserByLogin(String login);
}
