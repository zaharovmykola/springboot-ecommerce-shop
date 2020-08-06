package org.mykola.zakharov.spring.boot.first.ecommerceshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class Cart {

    private final List<CartItem> cartItems;

    public Cart() {
        cartItems = new ArrayList<>();
    }
}
