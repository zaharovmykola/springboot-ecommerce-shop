package org.mykola.zakharov.spring.boot.first.ecommerceshop.ui.pagefactory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/* PageObject для каркасной страницы index.html */
public class IndexPage extends AbstractPage {

    // пункт меню "Вход" находим на этапе выполнения конструктора
    // родительского класса
    @FindBy(css = "nav a[href*='signin']")
    private WebElement signInButton;

    @FindBy(css = "nav a[href*='#!home:out']")
    private WebElement signOutButton;

    @FindBy(css = "nav a[href*='#!shopping']")
    private WebElement shoppingButton;

    // для пункта "Выход" подготавливаем только селектор,
    // потому что он будет отображаться только после входа
    // private By logOutButton = By.cssSelector("nav a[href*='#!home:out']");

    public IndexPage(WebDriver driver) {
        super(driver);
        System.out.println("IndexPage Loaded");
    }

    public SignInPage clickSignIn() {
        signInButton.click();
        return new SignInPage(driver);
    }

    public SignInPage clickSignOut() {
        signOutButton.click();
        return new SignInPage(driver);
    }

    public ShoppingPage clickShopping() {
        shoppingButton.click();
        return new ShoppingPage(driver);
    }

    public String getLogOutButtonText() {
        /* List<WebElement> logOutButtonElement =
                driver.findElements(signOutButton);
        return !logOutButtonElement.isEmpty() ? logOutButtonElement.get(0).getText() : null; */
        return signOutButton.getText();
    }

    public Boolean isLogOutButtonDisplayed() {
        /* List<WebElement> logOutButtonElement =
                driver.findElements(logOutButton);
        return !logOutButtonElement.isEmpty() ? logOutButtonElement.get(0).getCssValue("display").equals("block") : false; */
        return signOutButton.getCssValue("display").equals("block");
    }
}
