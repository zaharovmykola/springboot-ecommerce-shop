package org.mykola.zakharov.spring.boot.first.ecommerceshop.dao;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderHibernateDAO extends JpaRepository<Order, Long> {

}