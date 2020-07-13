package org.mykola.zakharov.spring.boot.first.ecommerceshop.controller;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.ShoppingCart;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService service;

    @GetMapping("")
    public ResponseEntity<ResponseModel> getAllProductsByUser(@PathVariable String name) {
        return new ResponseEntity<>(service.getAllProductsByUser(name), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseModel> create(@RequestBody ShoppingCart shoppingCart) {
        return new ResponseEntity<>(service.create(shoppingCart), HttpStatus.CREATED);
    }
}
