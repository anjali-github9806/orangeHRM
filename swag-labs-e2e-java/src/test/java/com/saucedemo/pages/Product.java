package com.saucedemo.pages;

/**
 * Simple value object representing a product's name and price
 * as read from the inventory or cart page.
 */
public record Product(String name, double price) {
}
