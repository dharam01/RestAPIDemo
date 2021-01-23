package com.company.demo.entities.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaleProductsData {
    private List<SaleProduct> products;

    public List<SaleProduct> getProducts() {
        return products;
    }

    public void setProducts(List<SaleProduct> products) {
        this.products = products;
    }
}
