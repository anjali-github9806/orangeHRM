package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.base.TestData;
import com.saucedemo.pages.CartPage;
import com.saucedemo.pages.CheckoutPage;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.pages.Product;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class CheckoutFlowTest extends BaseTest {

    @Test(description = "Login, add all items under $29.99, verify cart, checkout, and confirm order")
    public void purchaseFlow_addsItemsUnderPriceAndCompletesOrder() {
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = new InventoryPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // 1. Login
        slowMo();
        loginPage.login(TestData.STANDARD_USERNAME, TestData.PASSWORD);
        inventoryPage.waitUntilLoaded();
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory.html"), "Should land on inventory page after login");
        slowMo();

        // 2. Add all products priced under $29.99 (data-driven, reads actual prices off the page)
        // slowMoMs is applied between each add-to-cart click inside addProductsUnderPrice itself.
        List<Product> addedProducts = inventoryPage.addProductsUnderPrice(TestData.MAX_PRICE);
        Assert.assertFalse(addedProducts.isEmpty(), "Expected at least one product under $" + TestData.MAX_PRICE);
        Assert.assertEquals(inventoryPage.getCartBadgeCount(), addedProducts.size(),
                "Cart badge count should match number of items added");
        slowMo();

        // 3. Open cart and verify items were added correctly
        inventoryPage.goToCart();
        cartPage.waitUntilLoaded();
        slowMo();

        Assert.assertEquals(cartPage.getCartItemCount(), addedProducts.size(),
                "Cart should contain exactly the items that were added");

        List<String> expectedNames = addedProducts.stream()
                .map(Product::name)
                .sorted()
                .collect(Collectors.toList());
        List<String> actualNames = cartPage.getCartItemNames().stream()
                .sorted()
                .collect(Collectors.toList());
        Assert.assertEquals(actualNames, expectedNames, "Cart items should match the products that were added");
        slowMo();

        // 4. Proceed to checkout and fill in customer information
        cartPage.checkout();
        wait.until(ExpectedConditions.urlContains("checkout-step-one.html"));
        slowMo();

        checkoutPage.fillInformation(
                TestData.CHECKOUT_FIRST_NAME,
                TestData.CHECKOUT_LAST_NAME,
                TestData.CHECKOUT_ZIP_CODE);
        slowMo();
        checkoutPage.continueToOverview();
        wait.until(ExpectedConditions.urlContains("checkout-step-two.html"));
        slowMo();

        // 5. Finish the order and verify confirmation
        checkoutPage.finish();
        wait.until(ExpectedConditions.urlContains("checkout-complete.html"));
        slowMo();

        Assert.assertTrue(checkoutPage.isOnCompletePage(), "Should land on the order confirmation page");
        Assert.assertEquals(checkoutPage.getConfirmationHeader(), "Thank you for your order!",
                "Order confirmation header should match expected text");
        Assert.assertFalse(checkoutPage.getConfirmationText().isBlank(),
                "Order confirmation text should be present");
    }

    @Test(description = "Checkout should block continue and show an error when required fields are empty")
    public void checkout_showsErrorWhenFieldsAreMissing() {
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = new InventoryPage(driver);
        CartPage cartPage = new CartPage(driver);
        CheckoutPage checkoutPage = new CheckoutPage(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        loginPage.login(TestData.STANDARD_USERNAME, TestData.PASSWORD);
        inventoryPage.waitUntilLoaded();
        inventoryPage.addProductsUnderPrice(TestData.MAX_PRICE);
        inventoryPage.goToCart();
        cartPage.waitUntilLoaded();
        cartPage.checkout();
        wait.until(ExpectedConditions.urlContains("checkout-step-one.html"));

        // Try to continue with an empty form
        checkoutPage.continueToOverview();

        Assert.assertTrue(checkoutPage.getErrorMessage().toLowerCase().contains("required"),
                "Should show a validation error for missing required fields");
        Assert.assertTrue(driver.getCurrentUrl().contains("checkout-step-one.html"),
                "Should remain on checkout step one when validation fails");
    }
}