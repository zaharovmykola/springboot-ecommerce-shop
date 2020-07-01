package org.mykola.zakharov.spring.boot.first.ecommerceshop.dao;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Roles;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHibernateDAO extends JpaRepository<Users, Long> {
    List<Users> findByVendor(String vendor);
}
