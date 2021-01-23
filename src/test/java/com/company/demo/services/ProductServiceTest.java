package com.company.demo.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {

    @Test
    void testGetProducts() {
        ProductService productService = new ProductService();
        assertTrue(productService.readFromFile().getProducts().size() > 0);
    }
}