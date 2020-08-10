package org.mykola.zakharov.spring.boot.first.ecommerceshop.dao;

import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Product;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.QProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductHibernateDAO extends JpaRepository<Product, Long>,
        QuerydslPredicateExecutor<Product>, QuerydslBinderCustomizer<QProduct> {

    // пользовательский метод получения списка товаров,
    // идентификаторы категорий которых входят в множество,
    // заданное параметром :ids,
    // который получает список идентификаторов категорий из объекта списка
    // @Param("ids") List<Long> categoryIds,
    // передаваемого при вызове метода в качестве аргумента
    // (явно задается JPQL-запрос, который должен выполнить модуль Spring Data)
    @Query( "SELECT p FROM Product p WHERE p.category.id IN :ids" )
    List<Product> findByCategoryIds(
            @Param("ids") List<Long> categoryIds,
            Sort sort
    );

    // добавление поддержки запросов query dsl
    // (предварительно нужно сгенерировать тип QProduct командой
    // mvn apt:process)
    @Override
    default public void customize(
            QuerydslBindings bindings, QProduct root) {
        bindings.bind(String.class)
                .first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
        bindings.excluding(root.image);
    }

    @Query( "SELECT MIN(p.price) FROM Product p" )
    BigDecimal findMinimum (); //{
        // if ()
        /* return productDao.findAll().stream()
            .min(
                (p1, p2) -> p1.getPrice().subtract(p2.getPrice()).intValue()
            ); */
    //}
    // find max price
    // 1. find all
    // 2. order by price DESC
    // 3. get top 1
    Product findTop1ByOrderByPriceDesc (); /* {
        return productDao.findAll().stream()
            .max(
                (p1, p2) -> p1.getPrice().subtract(p2.getPrice()).intValue()
            );
    } */
}
