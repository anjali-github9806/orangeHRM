package com.saucedemo.base;

/**
 * Centralized test data so tests aren't littered with magic strings/numbers.
 */
public final class TestData {

    private TestData() {
    }

    public static final String BASE_URL = "https://www.saucedemo.com/";

    public static final String STANDARD_USERNAME = "standard_user";
    public static final String LOCKED_OUT_USERNAME = "locked_out_user";
    public static final String PASSWORD = "secret_sauce";

    public static final String CHECKOUT_FIRST_NAME = "John";
    public static final String CHECKOUT_LAST_NAME = "Doe";
    public static final String CHECKOUT_ZIP_CODE = "411001";

    public static final double MAX_PRICE = 29.99;
}
