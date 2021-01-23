package com.company.demo.controllers;

import com.company.demo.components.ProductLoader;
import com.company.demo.entities.response.SaleProductsData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductRestControllerTest {

    private ProductRestController productRestController;
    @Mock
    private ProductLoader productLoader;

    @BeforeEach
    public void setup() {
        productLoader = mock(ProductLoader.class);
        SaleProductsData saleProductsData = mock(SaleProductsData.class);
        when(productLoader.getSaleProducts(anyString())).thenReturn(saleProductsData);
        productRestController = new ProductRestController(productLoader);
    }

    @Test
    public void testGetProductsWithPriceReduction() {
        assertNotNull(productRestController.getProductsWithPriceReduction("priceLabel"));
    }

}