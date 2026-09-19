package com.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Step one: information form
    private final By firstNameInput = By.cssSelector("[data-test='firstName']");
    private final By lastNameInput = By.cssSelector("[data-test='lastName']");
    private final By zipCodeInput = By.cssSelector("[data-test='postalCode']");
    private final By continueButton = By.cssSelector("[data-test='continue']");
    private final By errorMessage = By.cssSelector("[data-test='error']");

    // Step two: overview
    private final By finishButton = By.cssSelector("[data-test='finish']");

    // Step three: complete
    private final By completeHeader = By.cssSelector("[data-test='complete-header']");
    private final By completeText = By.cssSelector("[data-test='complete-text']");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void fillInformation(String firstName, String lastName, String zipCode) {
        driver.findElement(firstNameInput).sendKeys(firstName);
        driver.findElement(lastNameInput).sendKeys(lastName);
        driver.findElement(zipCodeInput).sendKeys(zipCode);
    }

    public void continueToOverview() {
        driver.findElement(continueButton).click();
    }

    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }

    public void finish() {
        wait.until(ExpectedConditions.elementToBeClickable(finishButton)).click();
    }

    public String getConfirmationHeader() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(completeHeader)).getText();
    }

    public String getConfirmationText() {
        return driver.findElement(completeText).getText();
    }

    public boolean isOnCompletePage() {
        return driver.getCurrentUrl().contains("checkout-complete.html");
    }
}
