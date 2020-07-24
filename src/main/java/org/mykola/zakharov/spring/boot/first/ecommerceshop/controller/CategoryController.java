package org.mykola.zakharov.spring.boot.first.ecommerceshop.controller;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Category;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.CategoryModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ResponseModel;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService service;

    @GetMapping("")
    public ResponseEntity<ResponseModel> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ResponseModel> create(@RequestBody CategoryModel category) {
        return new ResponseEntity<>(service.create(category), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<ResponseModel> update(@PathVariable Long id, @RequestBody CategoryModel category) {
        category.setId(id);
        return new ResponseEntity<>(service.update(category), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseModel> deleteCategory(@PathVariable Long id) {
        ResponseModel responseModel = service.delete(id);
        System.out.println(responseModel);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }
}
