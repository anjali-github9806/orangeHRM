package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class CartPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By cartItems = By.cssSelector("[data-test='inventory-item']");
    private final By itemName = By.cssSelector("[data-test='inventory-item-name']");
    private final By checkoutButton = By.cssSelector("[data-test='checkout']");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("cart.html"));
    }

    public List<String> getCartItemNames() {
        List<WebElement> items = driver.findElements(cartItems);
        List<String> names = new ArrayList<>();
        for (WebElement item : items) {
            names.add(item.findElement(itemName).getText());
        }
        return names;
    }

    public int getCartItemCount() {
        return driver.findElements(cartItems).size();
    }

    public void checkout() {
        driver.findElement(checkoutButton).click();
    }
}
