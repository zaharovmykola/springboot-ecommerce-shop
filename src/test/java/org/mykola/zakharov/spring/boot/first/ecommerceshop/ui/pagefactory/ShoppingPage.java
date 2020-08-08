package org.mykola.zakharov.spring.boot.first.ecommerceshop.ui.pagefactory;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ShoppingPage extends AbstractPage {

    private By shoppingButton = By.cssSelector("nav a[href*='#!shopping']");
    private By shoppingSettingsButton = By.id("slide-out");

    public ShoppingPage(WebDriver driver) {
        super(driver);
        System.out.println("ShoppingPage Loaded");
    }

    public ShoppingPage goingToShoppingPage() {
        driver.findElement(shoppingButton).click();
        return new ShoppingPage(driver);
    }

    public ShoppingPage goingToShoppingPageMenuFilter() {
        driver.findElement(shoppingButton).click();

        driver.findElement(shoppingSettingsButton)
                .findElement(By.className("collapsible"))
                .findElement(By.id("priceFrom"))
                .getText().replaceAll("56", "50");

        driver.findElement(shoppingSettingsButton)
                .findElement(By.className("collapsible"))
                .findElement(By.id("priceTo"))
                .getText().replaceAll("232", "200");


        return new ShoppingPage(driver);
    }

}
