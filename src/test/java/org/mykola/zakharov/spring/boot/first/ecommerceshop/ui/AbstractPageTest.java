package org.mykola.zakharov.spring.boot.first.ecommerceshop.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mykola.zakharov.spring.boot.first.ecommerceshop.ui.pagefactory.IndexPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;


import java.util.concurrent.TimeUnit;

public abstract class AbstractPageTest {

    protected static WebDriver driver;
    protected IndexPage indexPage;

    @BeforeAll
    private static void setupAll() {
        System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
    }

    @BeforeEach
    private void setupEach() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get("http://localhost:8090/eCommerceShop/");
        indexPage = new IndexPage(driver);
    }

    @AfterEach
    private void disposeEach() {
        driver.quit();
    }
}
