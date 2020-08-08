package org.mykola.zakharov.spring.boot.first.ecommerceshop.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.EcommerceShopApplication;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.ui.pagefactory.ShoppingPage;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = EcommerceShopApplication.class
)
public class ShoppingPageTest extends AbstractPageTest {

    private ShoppingPage shoppingPage;

    @BeforeEach
    public void setupCase() {
        shoppingPage = indexPage.clickShopping();
    }

    @Test
    @Order(1)
    public void performSignOutAsAdmin() throws InterruptedException {
        Thread.sleep(5000);
        shoppingPage.goingToShoppingPageMenuFilter();
        Thread.sleep(5000);

    }

}
