package org.mykola.zakharov.spring.boot.first.ecommerceshop.application.controller;

import org.junit.jupiter.api.Test;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.controller.ProductController;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.CategoryModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ProductFilterModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ProductModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ProductControllerMethodsTest {

    @Value("tests.unit.strings.image-base64")
    private String imageBase64;

    @Autowired
    private ProductController productController;

    @Test
    public void shouldReturnAllProducts () {
        productController.getAll();
        ResponseEntity responseEntity = productController.getAll();
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void shouldCreateProduct () {
        ProductModel productModel
                = ProductModel.builder()
                .title("test product 1")
                .description("about test product 1")
                .price(new BigDecimal(10.5))
                .quantity(5)
                .image(imageBase64)
                .categoryId(1L).build();
        ResponseEntity responseEntity = productController.create(productModel);
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void shouldUpdateProduct () {
        ProductModel productModel
                = ProductModel.builder()
                .id(1L)
                .title("test product 2")
                .description("about test product 2")
                .price(new BigDecimal(10.5))
                .quantity(5)
                .image(imageBase64)
                .categoryId(1L).build();
        productController.create(productModel);
        ResponseEntity responseEntityUpdate
                = productController.update(productModel.getId(), productModel);
        assertNotNull(responseEntityUpdate);
        assertEquals(responseEntityUpdate.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void shouldDeleteProduct () {
        ProductModel productModel
                = ProductModel.builder()
                .id(2L)
                .title("test product 1")
                .description("about test product 1")
                .price(new BigDecimal(10.5))
                .quantity(5)
                .image(imageBase64)
                .categoryId(1L).build();
        productController.create(productModel);
        ResponseEntity responseEntityDelete
                = productController.deleteProduct(productModel.getId());
        assertNotNull(responseEntityDelete);
        assertEquals(responseEntityDelete.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void shouldFilteredProductsByCategories () {
        final ProductFilterModel filter =
                ProductFilterModel.builder()
                        .categories(Arrays.asList(1L, 2L, 3L))
                        .orderBy("")
                        .sortingDirection(Sort.Direction.ASC)
                        .build();
        ResponseEntity responseEntityFilter
                = productController.getByCategories(filter.categories, filter.orderBy, filter.sortingDirection);
        assertNotNull(responseEntityFilter);
        assertEquals(responseEntityFilter.getStatusCode(), HttpStatus.OK);
    }

}
