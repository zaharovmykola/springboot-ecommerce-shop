package org.mykola.zakharov.spring.boot.first.ecommerceshop.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.EcommerceShopApplication;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.ui.pagefactory.SignInPage;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = EcommerceShopApplication.class
)
public class SignOutPageTest extends AbstractPageTest {

    private SignInPage signInPage;

    @BeforeEach
    public void setupCase() {
        signInPage = indexPage.clickSignIn();
    }

    @Test
    @Order(1)
    public void performSignOutAsAdmin() {
        signInPage.loginWithValidCredentials("admin", "AdminPassword1");
        assertEquals("http://localhost:8090/eCommerceShop/#!home", driver.getCurrentUrl());
        String logOutButtonText = indexPage.getLogOutButtonText();
        assertNotNull(logOutButtonText);
        assertEquals("Sign Out (admin)", logOutButtonText);

        signInPage.loginOut("admin", "AdminPassword1");
        assertEquals("http://localhost:8090/eCommerceShop/#!home", driver.getCurrentUrl());
        String logOutButtonTextAfterSignOut = indexPage.getLogOutButtonText();
        assertNull(logOutButtonTextAfterSignOut);
        assertEquals("", logOutButtonText);
    }

    @Test
    @Order(2)
    public void performSignOutAsUser() {
        signInPage.loginWithValidCredentials("one", "UserPassword1");
        assertEquals("http://localhost:8090/eCommerceShop/#!home", driver.getCurrentUrl());
        String logOutButtonText = indexPage.getLogOutButtonText();
        assertNotNull(logOutButtonText);
        assertEquals("Sign Out (admin)", logOutButtonText);

        signInPage.loginOut("one", "UserPassword1");
        assertEquals("http://localhost:8090/eCommerceShop/#!home", driver.getCurrentUrl());
        String logOutButtonTextAfterSignOut = indexPage.getLogOutButtonText();
        assertNull(logOutButtonTextAfterSignOut);
        assertEquals("", logOutButtonText);
    }

}