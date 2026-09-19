package com.saucedemo.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import java.time.Duration;

public class BaseTestClass {

    protected WebDriver driver;

    @BeforeMethod
    @Parameters("headless")
    public void setUp(@org.testng.annotations.Optional("false") String headless) {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(headless)) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1400,1000");
        options.addArguments("--disable-notifications");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0)); // rely on explicit waits
        driver.manage().window().maximize();
        driver.get(TestData.BASE_URL);
    }

    /**
     * Pauses briefly so a human watching the browser can see each action
     * happen. Controlled by the "slowMoMs" system property (milliseconds,
     * default 0 = off). Example: mvn test -DslowMoMs=800
     */
    protected void slowMo() {
        String ms = System.getProperty("slowMoMs", "10");
        try {
            Thread.sleep(Long.parseLong(ms));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (NumberFormatException ignored) {
            // invalid value, just skip the pause
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}