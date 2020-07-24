package org.mykola.zakharov.spring.boot.first.ecommerceshop.service;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.CategoryHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.ProductHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Category;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Product;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.CategoryModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ProductFilterModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ProductModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductHibernateDAO productDao;

    @Autowired
    private CategoryHibernateDAO categoryDao;

    public ResponseModel create(ProductModel productModel) {
        Optional<Category> categoryOptional
                = categoryDao.findById(productModel.getCategoryId());
        if(categoryOptional.isPresent()){
            Product product =
                Product.builder()
                    .name(productModel.getTitle().trim())
                    .description(productModel.getDescription().trim())
                    .price(productModel.getPrice())
                    .quantity(productModel.getQuantity())
                    .image(productModel.getImage())
                    .category(categoryOptional.get())
                    .build();
            System.out.println(product);
            productDao.save(product);
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("Product %s Created", product.getName()))
                    .build();
        } else {
            return ResponseModel.builder()
                .status(ResponseModel.FAIL_STATUS)
                .message(String.format("Category #%d Not Found", productModel.getCategoryId()))
                .build();
        }
    }

    public ResponseModel update(ProductModel productModel) {
        Optional<Category> categoryOptional
                = categoryDao.findById(productModel.getCategoryId());
        if(categoryOptional.isPresent()){
            Product category =
                    Product.builder()
                            .id(productModel.getId())
                            .name(productModel.getTitle().trim())
                            .description(productModel.getDescription().trim())
                            .price(productModel.getPrice())
                            .quantity(productModel.getQuantity())
                            .category(categoryOptional.get())
                            .build();
            productDao.save(category);
            // Demo Logging
            System.out.println(String.format("Category %s Updated", category.getName()));
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("Category %s Updated", category.getName()))
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Category #%d Not Found", productModel.getCategoryId()))
                    .build();
        }
    }

    public ResponseModel getAll() {
        List<Product> products = productDao.findAll(Sort.by("id").descending());
        List<ProductModel> productModels =
            products.stream()
                .map(p ->
                    ProductModel.builder()
                        .id(p.getId())
                        .title(p.getName().trim())
                        .description(p.getDescription().trim())
                        .price(p.getPrice())
                        .quantity(p.getQuantity())
                        .image(p.getImage())
                        .category(
                            CategoryModel.builder()
                                .id(p.getCategory().getId())
                                .name(p.getCategory().getName().trim())
                                .build()
                        )
                        .build()
                )
                .collect(Collectors.toList());
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .data(productModels)
                .build();
    }

    public ResponseModel delete(Long id) {
        Optional<Product> productOptional = productDao.findById(id);
        if (productOptional.isPresent()){
            Product product = productOptional.get();
            productDao.delete(product);
            return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("Product #%s Deleted", product.getName()))
                .build();
        } else {
            return ResponseModel.builder()
                .status(ResponseModel.FAIL_STATUS)
                .message(String.format("Product #%d Not Found", id))
                .build();
        }
    }

    public ResponseModel getFiltered(ProductFilterModel filter) {
        List<Product> products =
            productDao.findByCategoryIds(
                filter.categories,
                Sort.by(filter.sortingDirection, filter.orderBy)
            );
        List<ProductModel> productModels =
            products.stream()
            .map((p)->
                ProductModel.builder()
                    .title(p.getName().trim())
                    .description(p.getDescription().trim())
                    .price(p.getPrice())
                    .quantity(p.getQuantity())
                    .categoryId(p.getCategory().getId())
                    .build()
            )
            .collect(Collectors.toList());

        return ResponseModel.builder()
            .status(ResponseModel.SUCCESS_STATUS)
            .data(productModels)
            .build();
    }
}
