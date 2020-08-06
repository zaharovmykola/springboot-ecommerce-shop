package org.mykola.zakharov.spring.boot.first.ecommerceshop.controller;

import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.Cart;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.CartItem;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService productService;

    @GetMapping("")
    public ResponseEntity<ResponseModel> getCartItems(HttpSession httpSession) {
        Cart cart = (Cart) httpSession.getAttribute("CART");
        if (cart == null) {
            cart = new Cart();
        }
        return new ResponseEntity<>(productService.getCartItems(cart), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<ResponseModel> addCartItemCount(@PathVariable("id") Long id, HttpSession httpSession) {
        Cart cart = (Cart) httpSession.getAttribute("CART");
        if (cart == null) {
            cart = new Cart();
        }
        ResponseModel response =
                productService.changeCartItemCount(
                        cart
                        , id
                        , CartItem.Action.ADD
                );
        httpSession.setAttribute("CART", cart);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseModel> subtractCartItemCount(@PathVariable("id") Long id, HttpSession httpSession) throws InstantiationException, IllegalAccessException {
        Cart cart = (Cart) httpSession.getAttribute("CART");
        if (cart == null) {
            cart = new Cart();
        }
        ResponseModel response =
                productService.changeCartItemCount(
                        cart
                        , id
                        , CartItem.Action.SUB
                );
        httpSession.setAttribute("CART", cart);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<ResponseModel> deleteCartItem(@PathVariable("id") Long id, HttpSession httpSession) {
        Cart cart = (Cart) httpSession.getAttribute("CART");
        if (cart == null) {
            cart = new Cart();
        }
        ResponseModel response =
                productService.changeCartItemCount(
                        cart
                        , id
                        , CartItem.Action.REM
                );
        httpSession.setAttribute("CART", cart);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}