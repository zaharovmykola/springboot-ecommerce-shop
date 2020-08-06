package org.mykola.zakharov.spring.boot.first.ecommerceshop.service;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.ProductHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Product;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.Cart;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.CartItem;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private ProductHibernateDAO productDAO;

    public ResponseModel getCartItems(Cart cart) {
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message("Cart data fetched successfully")
                .data(cart.getCartItems())
                .build();
    }

    public ResponseModel changeCartItemCount(Cart cart, Long id, CartItem.Action action) {
        CartItem currentCartItem = null;
        Product product = productDAO.findById(id).get();
        List<CartItem> currentCartItemList =
                cart.getCartItems()
                        .stream()
                        .filter((item) -> item.getId().equals(id))
                        .collect(Collectors.toList());
        if (currentCartItemList.size() > 0) {
            currentCartItem = currentCartItemList.get(0);
        } else {
            currentCartItem = new CartItem(id, product.getName(), 0);
            cart.getCartItems().add(currentCartItem);
        }
        if (action != null) {
            switch (action) {
                case ADD:
                    currentCartItem.setCount(currentCartItem.getCount() + 1);
                    break;
                case SUB:
                    currentCartItem.setCount(currentCartItem.getCount() - 1);
                    if (currentCartItem.getCount() <= 0) {
                        cart.getCartItems().remove(currentCartItem);
                    }
                    break;
                case REM:
                    cart.getCartItems().remove(currentCartItem);
                    break;
                default:
                    break;
            }
        }
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message("Cart data changed successfully")
                .data(cart.getCartItems())
                .build();
    }
}
