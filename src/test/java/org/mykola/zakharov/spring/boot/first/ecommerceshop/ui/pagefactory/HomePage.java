package org.mykola.zakharov.spring.boot.first.ecommerceshop.ui.pagefactory;

import org.openqa.selenium.WebDriver;

public class HomePage extends AbstractPage {

    public HomePage(WebDriver driver) {
        super(driver);
        System.out.println("HomePage Loaded");
    }
}
