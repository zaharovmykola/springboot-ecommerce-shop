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
public class SignInPageTest extends AbstractPageTest {

    private SignInPage signInPage;

    @BeforeEach
    public void setupCase() {
        signInPage = indexPage.clickSignIn();
    }

    @Test
    @Order(1)
    public void performSignInWithCorrectAdminUserNameAndPassword() {
        signInPage.loginWithValidCredentials("admin", "AdminPassword1");
        assertEquals("http://localhost:8090/eCommerceShop/#!home", driver.getCurrentUrl());
        String logOutButtonText = indexPage.getLogOutButtonText();
        assertNotNull(logOutButtonText);
        assertEquals("Sign Out (admin)", logOutButtonText);
    }

    @Test
    @Order(2)
    public void failSignInWithWrongUserNameAndCorrectAdminPassword() {
        //signInPage =
        signInPage.loginWithInvalidCredentials("wrong", "AdminPassword1");
        String logOutButtonText = indexPage.getLogOutButtonText();
        assertEquals("", logOutButtonText);
        String errorText = signInPage.getErrorText();
        assertNotNull(errorText);
        assertEquals("Error: wrong username or password", errorText);
    }

    @Test
    @Order(3)
    public void performSignInWithCorrectUserNameAndPassword() {
        signInPage.loginWithValidCredentials("one", "UserPassword1");
        assertEquals("http://localhost:8090/eCommerceShop/#!home", driver.getCurrentUrl());
        String logOutButtonText = indexPage.getLogOutButtonText();
        assertNotNull(logOutButtonText);
        assertEquals("Sign Out (user)", logOutButtonText);
    }

    @Test
    @Order(4)
    public void failSignInWithWrongUserNameAndCorrectPassword() {
        //signInPage =
        signInPage.loginWithInvalidCredentials("wrong", "UserPassword1");
        String logOutButtonText = indexPage.getLogOutButtonText();
        assertEquals("", logOutButtonText);
        String errorText = signInPage.getErrorText();
        assertNotNull(errorText);
        assertEquals("Error: wrong username or password", errorText);
    }

    @Test
    @Order(5)
    public void failSignInWithoutUserNameAndCorrectPassword() {
        //signInPage =
        signInPage.loginWithInvalidCredentials("", "UserPassword1");
        String logOutButtonText = indexPage.getLogOutButtonText();
        assertEquals("", logOutButtonText);
        String errorText = signInPage.getErrorText();
        assertNotNull(errorText);
        assertEquals("Error: wrong username or password", errorText);
    }

    @Test
    @Order(6)
    public void failSignInWithCorrectUserNameAndWithoutPassword() {
        //signInPage =
        signInPage.loginWithInvalidCredentials("one", "");
        String logOutButtonText = indexPage.getLogOutButtonText();
        assertEquals("", logOutButtonText);
        String errorText = signInPage.getErrorText();
        assertNotNull(errorText);
        assertEquals("Error: wrong username or password", errorText);
    }

    @Test
    @Order(7)
    public void failSignInWithoutUserNameAndPassword() {
        //signInPage =
        signInPage.loginWithInvalidCredentials("", "");
        String logOutButtonText = indexPage.getLogOutButtonText();
        assertEquals("", logOutButtonText);
        String errorText = signInPage.getErrorText();
        assertNotNull(errorText);
        assertEquals("Error: wrong username or password", errorText);
    }

}
