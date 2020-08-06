package org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.predicate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.criteria.SearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
/*
 * Строитель составного выражения фильтрации запрашиваемых данных
 * из набора предикатов
 * */
public class ProductPredicatesBuilder {

    private List<SearchCriteria> params;

    public ProductPredicatesBuilder() {
        params = new ArrayList<>();
    }

    // обеспечение возможности добавления предикатов в запрос query dsl
    // цепочкой вызовов метода with,
    // принимающего три составляющие для каждого предиката
    // согласно структуры исходных данных SearchCriteria
    public ProductPredicatesBuilder with(
            String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    // построение запроса из всей цепочки предикатов
    public BooleanExpression build() {
        if (params.size() == 0) {
            return null;
        }
        // на основе каждой структуры входных данных
        // формируем предикат,
        // отсеиваем только успешно созданные предикаты,
        // отбрасывая возможные выходные элементы потока со значением null
        List<BooleanExpression> predicates = params.stream().map(param -> {
            ProductPredicate predicate = new ProductPredicate(param);
            try {
                return predicate.getPredicate();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        // собираем список предикатов в результирующее составное условие
        // отбора результатов запроса к источнику данных
        BooleanExpression result = Expressions.asBoolean(true).isTrue();
        for (BooleanExpression predicate : predicates) {
            result = result.and(predicate);
        }
        return result;
    }
}
