package org.mykola.zakharov.spring.boot.first.ecommerceshop.dao;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Category;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryHibernateDAO extends JpaRepository<Category, Long> {
    List<Category> findByVendor(String vendor);
}
