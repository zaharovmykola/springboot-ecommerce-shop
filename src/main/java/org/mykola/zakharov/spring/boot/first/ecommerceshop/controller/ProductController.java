package org.mykola.zakharov.spring.boot.first.ecommerceshop.controller;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Product;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ProductFilterModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ProductModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping("")
    public ResponseEntity<ResponseModel> getAll() {
        return new ResponseEntity<>(service.getAll(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ResponseModel> create(@RequestBody ProductModel product) {
        return new ResponseEntity<>(service.create(product), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<ResponseModel> update(@PathVariable Long id, @RequestBody ProductModel product) {
        product.setId(id);
        return new ResponseEntity<>(service.update(product), HttpStatus.OK);
    }

    @GetMapping("/categories/{categoryIds}/products::orderBy:{orderBy}::sortingDirection:{sortingDirection}")
    public ResponseEntity<ResponseModel> getByCategories(
            @PathVariable List<Long> categoryIds,
            @PathVariable String orderBy,
            @PathVariable Sort.Direction sortingDirection
    ) {
        return new ResponseEntity<>(
                service.getFiltered(
                        new ProductFilterModel(categoryIds, orderBy, sortingDirection)
                ),
                HttpStatus.OK
        );
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseModel> deleteProduct(@PathVariable Long id) {
        ResponseModel responseModel = service.delete(id);
        System.out.println(responseModel);
        return new ResponseEntity<>(responseModel, HttpStatus.OK);
    }
}
