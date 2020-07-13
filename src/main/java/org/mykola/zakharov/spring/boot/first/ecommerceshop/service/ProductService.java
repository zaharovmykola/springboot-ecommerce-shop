package org.mykola.zakharov.spring.boot.first.ecommerceshop.service;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.ProductHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Category;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Product;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ProductService {
    @Autowired
    private ProductHibernateDAO dao;

    public ResponseModel create(Product product) {
        dao.save(product);
        return ResponseModel.builder()
                .status("success")
                .message(String.format("Product %s created", product.getTitle()))
                .build();
    }

    public ResponseModel getAll() {
        List<Product> products = dao.findAll();
        return ResponseModel.builder()
                .status("success")
                .data(products)
                .build();
    }
}
