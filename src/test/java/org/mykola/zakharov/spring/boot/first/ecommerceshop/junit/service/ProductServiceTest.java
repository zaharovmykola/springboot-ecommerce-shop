package org.mykola.zakharov.spring.boot.first.ecommerceshop.junit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.CategoryHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.ProductHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Category;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Product;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ProductModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.service.CategoryService;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.service.ProductService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductHibernateDAO productDAO;

    @Mock
    private CategoryHibernateDAO categoryDAO;

    @InjectMocks
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    ArgumentCaptor<Product> productArgument =
            ArgumentCaptor.forClass(Product.class);
    ArgumentCaptor<Category> categoryArgument =
            ArgumentCaptor.forClass(Category.class);

    @Test
    void shouldCreatedProductSuccessfully() {
        final ProductModel productModel =
                ProductModel.builder()
                        .title("test product 1")
                        .description("about test product 1")
                        .price(new BigDecimal(10.5))
                        .quantity(5)
                        //.image()   ?????????
                        .categoryId(Long.valueOf(1))
                        .build();

        Optional<Category> categoryOptional =
                categoryDAO.findById(productModel.getCategoryId());

        ResponseModel responseModel =
                productService.create(productModel);
        // Проверка, что результат не равен null
        assertNotNull(responseModel);
        // Проверка, что результат содержит положительный статус-код
        assertEquals(ResponseModel.SUCCESS_STATUS, responseModel.getStatus());
        // Проверка, что в результате вызванного выше метода (create)
        // минимум 1 раз был вызван метод save
        verify(productDAO, atLeast(1))
                .save(productArgument.capture());
        verify(categoryDAO, atLeast(1))
                .save(categoryArgument.capture());
    }

}
