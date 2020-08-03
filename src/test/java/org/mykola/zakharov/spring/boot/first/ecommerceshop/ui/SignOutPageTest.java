package org.mykola.zakharov.spring.boot.first.ecommerceshop.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.EcommerceShopApplication;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.ui.pagefactory.SignInPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT,
        classes = EcommerceShopApplication.class
)
public class SignOutPageTest extends AbstractPageTest {

    private By signOutButton = By.cssSelector("nav a[href='#!home:out']");
    private By logOutButton = By.cssSelector("nav a[href*='#!home:out']");

    private SignInPage signInPage;

    @BeforeEach
    public void setupCase() {
        signInPage = indexPage.clickSignIn();
    }

    @Test
    @Order(1)
    public void performSignOutAsAdmin() throws InterruptedException {
        signInPage.loginWithValidCredentials("admin", "AdminPassword1");
        Thread.sleep(2000);
        assertEquals("http://localhost:8090/eCommerceShop/#!home", driver.getCurrentUrl());
        String logOutButtonText = indexPage.getLogOutButtonText();
        assertNotNull(logOutButtonText);
        assertEquals("Sign Out (admin)", logOutButtonText);

        signInPage.loginOut("admin", "AdminPassword1");
        Thread.sleep(2000);
        assertEquals("http://localhost:8090/eCommerceShop/#!home", driver.getCurrentUrl());
        String logOutButtonTextAfterSignOut = indexPage.getLogOutButtonText();
        assertEquals("", logOutButtonTextAfterSignOut);
    }

    @Test
    @Order(2)
    public void performSignOutAsUser() throws InterruptedException {
        signInPage.loginWithValidCredentials("one", "UserPassword1");
        Thread.sleep(2000);
        assertEquals("http://localhost:8090/eCommerceShop/#!home", driver.getCurrentUrl());
        String logOutButtonText = indexPage.getLogOutButtonText();
        assertNotNull(logOutButtonText);
        assertEquals("Sign Out (one)", logOutButtonText);

        signInPage.loginOut("one", "UserPassword1");
        Thread.sleep(2000);
        assertEquals("http://localhost:8090/eCommerceShop/#!home", driver.getCurrentUrl());
        String logOutButtonTextAfterSignOut = indexPage.getLogOutButtonText();
        assertEquals("", logOutButtonTextAfterSignOut);
    }

    @Test
    @Order(3)
    public void checkingSignOutStyleAndTextAvailability() throws InterruptedException {
        signInPage.loginWithValidCredentials("one", "UserPassword1");
        Thread.sleep(5000);
        indexPage.getLogOutButtonText();
        List<WebElement> logOutButtonElementAfterLogIn =
                driver.findElements(logOutButton);
        assertTrue(logOutButtonElementAfterLogIn.contains("display: block;"));

        signInPage.loginOut("one", "UserPassword1");
        Thread.sleep(5000);
        List<WebElement> logOutButtonElementAfterLogOut =
                driver.findElements(logOutButton);
        assertTrue(logOutButtonElementAfterLogOut.contains("display: none;"));
    }

}