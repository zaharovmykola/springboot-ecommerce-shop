package org.mykola.zakharov.spring.boot.first.ecommerceshop.ui.pagefactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public abstract class AbstractPage {

    protected WebDriver driver;

    public AbstractPage(WebDriver driver) {
        this.driver = driver;
        // запуск контейнера внедрения зависимостей -
        // ссылок на веб-элементы, найденные по селекторам аннотаций @FindBy
        PageFactory.initElements(driver, this);
    }
}
