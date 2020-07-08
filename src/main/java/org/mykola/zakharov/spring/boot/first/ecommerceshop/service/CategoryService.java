package org.mykola.zakharov.spring.boot.first.ecommerceshop.service;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.CategoryHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Category;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryHibernateDAO dao;

    public ResponseModel create(Category category) {
        dao.save(category);
        return ResponseModel.builder()
                .status("success")
                .message(String.format("Category %s created", category.getName()))
                .build();
    }

    public ResponseModel getAll() {
        List<Category> categories = dao.findAll();
        return ResponseModel.builder()
                .status("success")
                .data(categories)
                .build();
    }
}
