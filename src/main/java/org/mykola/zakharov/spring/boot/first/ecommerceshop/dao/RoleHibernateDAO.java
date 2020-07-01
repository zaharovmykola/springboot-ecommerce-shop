package org.mykola.zakharov.spring.boot.first.ecommerceshop.dao;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleHibernateDAO extends JpaRepository<Roles, Long> {
    List<Roles> findByVendor(String vendor);
}
