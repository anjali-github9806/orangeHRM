package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class InventoryPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By inventoryContainer = By.cssSelector("[data-test='inventory-container']");
    private final By inventoryItems = By.cssSelector("[data-test='inventory-item']");
    private final By itemName = By.cssSelector("[data-test='inventory-item-name']");
    private final By itemPrice = By.cssSelector("[data-test='inventory-item-price']");
    private final By addToCartButton = By.cssSelector("button.btn_inventory");
    private final By cartLink = By.cssSelector("[data-test='shopping-cart-link']");
    private final By cartBadge = By.cssSelector("[data-test='shopping-cart-badge']");

    public InventoryPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void waitUntilLoaded() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(inventoryContainer));
    }

    /**
     * Adds every product whose price is strictly less than maxPrice.
     * Reads name + price directly off the page, so it stays correct
     * even if Sauce Labs changes prices or the catalog.
     *
     * @return the list of products that were added
     */
    public List<Product> addProductsUnderPrice(double maxPrice) {
        List<WebElement> items = driver.findElements(inventoryItems);
        List<Product> added = new ArrayList<>();

        for (WebElement item : items) {
            String name = item.findElement(itemName).getText();
            String priceText = item.findElement(itemPrice).getText().replace("$", "");
            double price = Double.parseDouble(priceText);

            if (price < maxPrice) {
                item.findElement(addToCartButton).click();
                added.add(new Product(name, price));
            }
        }
        return added;
    }

    public int getCartBadgeCount() {
        List<WebElement> badges = driver.findElements(cartBadge);
        if (badges.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(badges.get(0).getText());
    }

    public void goToCart() {
        driver.findElement(cartLink).click();
    }
}
