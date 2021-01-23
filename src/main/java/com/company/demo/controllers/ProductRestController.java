package com.company.demo.controllers;

import com.company.demo.components.ProductLoader;
import com.company.demo.entities.response.SaleProductsData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "search/api/rest/v2/catalog/products")
public class ProductRestController {
    private final static Logger LOGGER = LoggerFactory.getLogger(ProductRestController.class.getName());
    private final ProductLoader productLoader;

    public ProductRestController(final ProductLoader productLoader) {
        this.productLoader = productLoader;
    }

    @GetMapping(path = "/reductions", produces = "application/json")
    public SaleProductsData getProductsWithPriceReduction(@RequestParam(name = "labelType", required = false) final String labelType) {
        LOGGER.debug("Get products on reductions request price label type {}.", labelType);
        return productLoader.getSaleProducts(labelType);
    }
}
