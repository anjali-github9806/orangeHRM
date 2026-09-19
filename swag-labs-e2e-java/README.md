# Swag Labs E2E Tests (Selenium + Java + TestNG)

End-to-end tests for [saucedemo.com](https://www.saucedemo.com/), built with
Selenium WebDriver, TestNG, and Maven, using the Page Object Model.

## What the main test does

1. Logs in as `standard_user`.
2. Reads every product's price on the inventory page and adds to the cart
   every product priced **strictly under $29.99** (currently: Sauce Labs
   Bike Light, Sauce Labs Bolt T-Shirt, Sauce Labs Onesie, and
   Test.allTheThings() T-Shirt (Red)). This is data-driven — it reads real
   prices off the page, so it still works if Sauce Labs changes prices or
   the catalog.
3. Opens the cart and verifies the item count and item names match exactly
   what was added.
4. Goes to checkout, fills in first name, last name, and zip code, continues.
5. Finishes the order and verifies the "Thank you for your order!"
   confirmation.

A second test verifies checkout blocks you with a validation error if you
try to continue with an empty information form.

## Project structure

```
swag-labs-e2e-java/
├── pom.xml
├── testng.xml
└── src/test/java/com/saucedemo/
    ├── base/
    │   ├── BaseTest.java     # WebDriver setup/teardown (WebDriverManager)
    │   └── TestData.java     # Credentials, checkout info, price threshold
    ├── pages/                # Page Object Model classes
    │   ├── LoginPage.java
    │   ├── InventoryPage.java
    │   ├── CartPage.java
    │   ├── CheckoutPage.java
    │   └── Product.java      # name/price record
    └── tests/
        └── CheckoutFlowTest.java
```

## Prerequisites

- Java 17+
- Maven 3.8+
- Google Chrome installed (WebDriverManager auto-downloads the matching
  chromedriver — no manual driver setup needed)

## Run the tests

```bash
mvn test
```

Run headless (e.g. in CI):

```bash
mvn test -DheadlessParam=true
```
> Note: `headless` is wired as a TestNG `@Parameters("headless")` value on
> `BaseTest.setUp`. If you want to drive it from the Maven command line,
> add a matching `<parameter>` in `testng.xml` or pass
> `-Dheadless=true` and read it via `System.getProperty("headless")` instead —
> either approach works, pick whichever fits your CI setup.

Run a single test class:

```bash
mvn test -Dtest=CheckoutFlowTest
```

## Notes / things you may want to tweak

- Credentials, checkout info, and the price threshold live in
  `TestData.java` — edit there rather than in the test class.
- Locators use `[data-test='...']` CSS selectors, matching Sauce Labs' own
  testing hooks, so they're resistant to styling/class-name changes.
- `BaseTest` uses explicit `WebDriverWait`s rather than a blanket implicit
  wait, which is the recommended Selenium practice and avoids flaky mixed
  waits.
- Only a Chrome profile is wired up by default; add a Firefox/Edge variant
  in `BaseTest` (WebDriverManager supports both) if you need cross-browser
  runs.
