package org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.predicate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import org.apache.commons.lang3.StringUtils;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.criteria.SearchCriteria;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Product;


import java.util.Arrays;
import java.util.List;

/* Динамически создаваемый предикат для поисковых запросов query dsl */
public class ProductPredicate {

    // входящие данные для построения предиката
    private SearchCriteria criteria;

    public ProductPredicate(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    public BooleanExpression getPredicate() throws JsonProcessingException {
        // создаем строитель предиката относительно сущности Товар
        PathBuilder<Product> entityPath =
                new PathBuilder<>(Product.class, "product");
        // ветвление в зависимости от типа полученного значения
        // (для проверки, является ли по содержанию строка числом,
        // в фале pom.xml подключна зависимость commons-lang3)
        if (StringUtils.isNumeric(criteria.getValue().toString())) {
            // если получено число (например, цена или количество товара) -
            // создаем основу предиката согласно заданного имени поля сущности Товар
            NumberPath<Integer> path =
                    entityPath.getNumber(criteria.getKey(), Integer.class);
            // ... и преобразуем значение к целому типу
            int value = Integer.parseInt(criteria.getValue().toString());
            // возвращается выражение сравнения чисел
            switch (criteria.getOperation()) {
                // значение поля должно быть строго равно значению value
                case ":":
                    return path.eq(value);
                // значение поля должно быть больше значения value, исключая его
                case ">":
                    return path.gt(value);
                // значение поля должно быть меньше значения value, исключая его
                case "<":
                    return path.lt(value);
                // значение поля должно быть больше значения value, исключая его
                case ">:":
                    return path.goe(value);
                // значение поля должно быть меньше значения value, исключая его
                case "<:":
                    final int SQL_ROUND_COMPENSATION = 1;
                    return path.loe(value + SQL_ROUND_COMPENSATION);
            }
        } else if (criteria.getValue().toString().startsWith("[")) {
            // если первый символ значения - открывающая квадратная скобка массива -
            // меняем объект строитель так, чтобы он работал с сущностью Товар,
            // но строил предикат относительно идентификатора,
            // содержащегося в заданном поле сущности товар
            // (например, идентификатор категории, к которой относится товар)
            entityPath =
                    new PathBuilder<>(Product.class, criteria.getKey() + ".id");
            // строковое значение формата json array преобразовываем в список чисел
            List value =
                    Arrays.asList(
                            new ObjectMapper().readValue(
                                    criteria.getValue().toString(),
                                    Long[].class
                            )
                    );
            //
            return entityPath.in(value);
        } else {
            // если полученное значение - не число и не множество -
            // строим предикат строгого сравнения строк
            StringPath path = entityPath.getString(criteria.getKey());
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                return path.containsIgnoreCase(criteria.getValue().toString());
            }
        }
        return null;
    }
}
