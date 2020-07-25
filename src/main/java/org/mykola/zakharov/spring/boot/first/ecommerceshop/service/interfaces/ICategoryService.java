package org.mykola.zakharov.spring.boot.first.ecommerceshop.service.interfaces;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.CategoryModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ResponseModel;

public interface ICategoryService {
    ResponseModel create(CategoryModel categoryModel);
    ResponseModel getAll();
    ResponseModel delete(Long id);
}
