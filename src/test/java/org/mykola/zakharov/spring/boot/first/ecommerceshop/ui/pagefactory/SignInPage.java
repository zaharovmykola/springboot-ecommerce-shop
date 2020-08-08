package org.mykola.zakharov.spring.boot.first.ecommerceshop.ui.pagefactory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SignInPage extends AbstractPage {

    private By loginField = By.id("username");
    private By passwordField = By.id("password");
    private By signInButton = By.cssSelector("#signin-form button[type='submit']");
    private By errorParagraph = By.cssSelector(".error");

    public SignInPage(WebDriver driver) {
        super(driver);
        System.out.println("SignInPage Loaded");
    }

    public SignInPage typeUserName(String userName) {
        driver.findElement(loginField).sendKeys(userName);
        return this;
    }

    public SignInPage typePassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
        return this;
    }

    public SignInPage loginWithInvalidCredentials(String userName, String password) {
        this.typeUserName(userName);
        this.typePassword(password);
        driver.findElement(signInButton).click();
        return new SignInPage(driver);
    }

    public HomePage loginWithValidCredentials(String userName, String password) {
        this.typeUserName(userName);
        this.typePassword(password);
        driver.findElement(signInButton).click();
        return new HomePage(driver);
    }

    public String getErrorText() {
        List<WebElement> errorParagraphElement =
                driver.findElements(errorParagraph);
        return !errorParagraphElement.isEmpty() ? errorParagraphElement.get(0).getText() : null;
    }

    public Boolean isLoginFieldValid() {
        return !driver.findElement(loginField).getAttribute("class").contains("invalid");
    }

    public Boolean isPasswordFieldValid() {
        return !driver.findElement(passwordField).getAttribute("class").contains("invalid");
    }
}
