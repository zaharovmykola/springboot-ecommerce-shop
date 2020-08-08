package org.mykola.zakharov.spring.boot.first.ecommerceshop.junit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.dao.ProductHibernateDAO;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.entity.Product;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.Cart;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.CartItem;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.model.ResponseModel;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.service.CartService;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Value("tests.unit.strings.image-base64")
    private String imageBase64;

    // Внедрение экземпляра CartService для его дальнейшего тестирования -
    // так, что при создании в него внедрятся все необходимые зависимости,
    // помеченные в классе тестов аннтацией @Mock
    @InjectMocks
    private CartService cartService;

    @Mock
    private ProductHibernateDAO productDAO;

    @Test
    void shouldGetCartItemsSuccessfully() {
        ResponseModel responseModel =
                cartService.getCartItems(returnCart());
        // Проверка, что результат не равен null
        assertNotNull(responseModel);
        assertNotNull(responseModel.getData());
        // Проверка, что результат содержит положительный статус-код
        assertEquals(ResponseModel.SUCCESS_STATUS, responseModel.getStatus());
    }

    @Test
    void shouldAddCartItemCountSuccessfully() {

        // что вернуть? - объект типа сущность Product
        doReturn(Optional.of(
                Product.builder()
                        .id(1L)
                        .name("water")
                        .description("about water")
                        .price(new BigDecimal(10.5))
                        .quantity(5)
                        .image(imageBase64)
                        .build()
        )).when(productDAO) // откуда? - из объекта productDAO
                .findById(1L); // когда? - когда в метод findById передан аргумент 1

        ResponseModel responseModelAdd =
                cartService.changeCartItemCount(returnCart(), 1L, CartItem.Action.ADD);
        // Проверка, что результат не равен null
        assertNotNull(responseModelAdd);
        assertNotNull(responseModelAdd.getData());
        // Проверка, что результат содержит положительный статус-код
        assertEquals(ResponseModel.SUCCESS_STATUS, responseModelAdd.getStatus());
    }

    @Test
    void shouldSubstractCartItemCountSuccessfully() {
        // что вернуть? - объект типа сущность Product
        doReturn(Optional.of(
                Product.builder()
                        .id(2L)
                        .name("juice")
                        .description("about juice")
                        .price(new BigDecimal(20.3))
                        .quantity(12)
                        .image(imageBase64)
                        .build()
        )).when(productDAO) // откуда? - из объекта productDAO
                .findById(2L); // когда? - когда в метод findById передан аргумент 2

        ResponseModel responseModelSub =
                cartService.changeCartItemCount(returnCart(), 2L, CartItem.Action.SUB);
        // Проверка, что результат не равен null
        assertNotNull(responseModelSub);
        assertNotNull(responseModelSub.getData());
        // Проверка, что результат содержит положительный статус-код
        assertEquals(ResponseModel.SUCCESS_STATUS, responseModelSub.getStatus());
    }

    @Test
    void shouldDeleteCartItemCountSuccessfully() {
        // что вернуть? - объект типа сущность Product
        doReturn(Optional.of(
                Product.builder()
                        .id(3L)
                        .name("fresh")
                        .description("about fresh")
                        .price(new BigDecimal(25.1))
                        .quantity(7)
                        .image(imageBase64)
                        .build()
        )).when(productDAO) // откуда? - из объекта productDAO
                .findById(3L); // когда? - когда в метод findById передан аргумент 2

        ResponseModel responseModelRem =
                cartService.changeCartItemCount(returnCart(), 3L, CartItem.Action.REM);
        // Проверка, что результат не равен null
        assertNotNull(responseModelRem);
        assertNotNull(responseModelRem.getData());
        // Проверка, что результат содержит положительный статус-код
        assertEquals(ResponseModel.SUCCESS_STATUS, responseModelRem.getStatus());
    }

    Cart returnCart () {
        List<CartItem> cartItems = new LinkedList<>();
        cartItems.add(new CartItem(1L, "water", 5, new BigDecimal(10.5)));
        cartItems.add(new CartItem(2L, "juice", 12, new BigDecimal(20.3)));
        cartItems.add(new CartItem(3L, "fresh", 7, new BigDecimal(25.1)));
        return new Cart(cartItems);
    }

}
