package org.mykola.zakharov.spring.boot.first.ecommerceshop.service;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.ShoppingCartHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.UserHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.ShoppingCart;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ProductModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShoppingCartService {
    @Autowired
    private ShoppingCartHibernateDAO daoShopCart;
    @Autowired
    private UserHibernateDAO daoUser;


    public ResponseModel create(ShoppingCart shoppingCart) {
        daoShopCart.save(shoppingCart);
        return ResponseModel.builder()
                .status("success")
                .message(String.format("ShoppingCart for user %s created", shoppingCart.getUser().getName()))
                .build();
    }

    public ResponseModel getAllProductsByUser(String name) {
        if (daoUser.findUserByName(name).getName() != null) {
            ShoppingCart shoppingCart = daoUser.findUserByName(name).getShoppingCart();
            List<ProductModel> allProductsByUser = shoppingCart
                    .getSetOfProducts().stream().map(product ->
                            ProductModel.builder()
                    .title(product.getName())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .quantity(product.getQuantity())
                    .image(product.getImage())
                    .category_id(product.getCategory().getId())
                    .build()
                    )
                    .collect(Collectors.toList());
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("List of Products in ShoppingCart for User: %s Retrieved Successfully", name))
                    .data(allProductsByUser)
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message("User" + name + " Not Found")
                    .build();
        }
    }
}
