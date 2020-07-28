package org.mykola.zakharov.spring.boot.first.ecommerceshop.application.controller;

import org.junit.jupiter.api.Test;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.controller.CategoryController;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.CategoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CategoryControllerMethodsTest {

    @Autowired
    private CategoryController categoryController;

    @Test
    public void shouldReturnAllCategories () {
        categoryController.getAll();
        ResponseEntity responseEntity = categoryController.getAll();
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void shouldCreateCategory () {
        CategoryModel categoryModel
                = CategoryModel.builder()
                .name("test category 1").build();
        ResponseEntity responseEntity = categoryController.create(categoryModel);
        assertNotNull(responseEntity);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
    }

    @Test
    public void shouldUpdateCategory () {
        CategoryModel categoryModel
                = CategoryModel.builder()
                .id(1L).name("test category 2").build();
        categoryController.create(categoryModel);
        ResponseEntity responseEntityUpdate
                = categoryController.update(categoryModel.getId(), categoryModel);
        assertNotNull(responseEntityUpdate);
        assertEquals(responseEntityUpdate.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void shouldDeleteCategory () {
        CategoryModel categoryModel
                = CategoryModel.builder()
                .id(2L).name("test category 3").build();
        categoryController.create(categoryModel);
        ResponseEntity responseEntityDelete
                = categoryController.deleteCategory(categoryModel.getId());
        assertNotNull(responseEntityDelete);
        assertEquals(responseEntityDelete.getStatusCode(), HttpStatus.OK);
    }

}
