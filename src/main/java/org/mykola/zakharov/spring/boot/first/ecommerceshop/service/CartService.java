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

    // изменить число определенного товара в объекте корзины
    public ResponseModel changeCartItemCount(Cart cart, Long productId, CartItem.Action action) {
        CartItem currentCartItem = null;
        // в БД находим описание товара по его ИД
        Product product = productDAO.findById(productId).get();
        // в объекте корзины пытаемся найти элемент списка товаров в корзине,
        // у которого ИД описания товара такой же, как заданный для изменения
        List<CartItem> currentCartItemList =
                cart.getCartItems()
                        .stream()
                        .filter((item) -> item.getId().equals(productId))
                        .collect(Collectors.toList());
        // если в корзине уже был хотя бы один такой товар
        if (currentCartItemList.size() > 0) {
            currentCartItem = currentCartItemList.get(0);
        } else {
            // если нет - добавляем товар в корзину с указанием его числа 0
            currentCartItem = new CartItem(productId, product.getName(), 0, product.getPrice());
            cart.getCartItems().add(currentCartItem);
        }
        if (action != null) {
            switch (action) {
                case ADD:
                    // увеличение числа товара в корзтине на 1
                    currentCartItem.setCount(currentCartItem.getCount() + 1);
                    break;
                case SUB:
                    // уменьшение числа товара в корзтине на 1,
                    // но если осталось 0 или меньше - полное удаление товара из корзины
                    currentCartItem.setCount(currentCartItem.getCount() - 1);
                    if (currentCartItem.getCount() <= 0) {
                        cart.getCartItems().remove(currentCartItem);
                    }
                    break;
                case REM:
                    // безусловное полное удаление товара из корзины
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
