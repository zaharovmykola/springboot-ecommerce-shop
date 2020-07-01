package org.mykola.zakharov.spring.boot.first.ecommerceshop.controller;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.CategoryResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.RoleResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.CategoryModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.RoleModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.service.CategoryService;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eCommerceShop")
public class CategoryController {
    @Autowired
    private CategoryService service;

    @PostMapping("/addCategory")
    public CategoryResponseModel addInstant(@RequestBody CategoryModel category) {
        return service.add(category);
    }

    @GetMapping("/getCategoriesByVendor/{vendor}")
    public CategoryResponseModel getCategory(@PathVariable String vendor) {
        return service.getCategory(vendor);
    }

    @GetMapping("/getAllCategories")
    public List<CategoryModel> getAllCategories() {
        return service.getAllCategories();
    }
}
