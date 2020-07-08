package org.mykola.zakharov.spring.boot.first.ecommerceshop.service;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.CategoryHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Category;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.CategoryResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.CategoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryService {
    @Autowired
    private CategoryHibernateDAO dao;

    public CategoryResponseModel add(CategoryModel categoryModel) {
        Category categories =
                Category.builder()
                        .vendor(categoryModel.getVendor())
                        .build();
        dao.save(categories);
        CategoryResponseModel response =
                CategoryResponseModel.builder()
                        .status("success")
                        .message("Next category added successfully :" + categories.getVendor())
                        .build();
        return response;
    }

    /* public CategoryResponseModel getCategory(String vendor) {
        List<Category> categories = dao.findByVendor(vendor);
        List <CategoryModel> categoryModels = categories.stream().map((categor) ->
                CategoryModel.builder()
                        .id(categor.getId())
                        .vendor(categor.getVendor())
                        .build()
        ).collect(Collectors.toList());
        return CategoryResponseModel.builder()
                .status("success")
                .categories(categoryModels)
                .build();
    } */

    public List<CategoryModel> getAllCategories () {
        List<Category> categories = dao.findAll();
        List <CategoryModel> categoryModels = categories.stream().map((categ) ->
                CategoryModel.builder()
                        .id(categ.getId())
                        .vendor(categ.getVendor())
                        .build()
        ).collect(Collectors.toList());
        return categoryModels;
    }
}
