package com.company.demo.entities.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaleProduct {
    private String productId;
    private String title;
    private List<SaleColorSwatch> colorSwatches;
    private String nowPrice;
    private String priceLabel;
    @JsonIgnore
    private Double priceReduction;

    public SaleProduct() {
        productId = "";
        title = "";
        nowPrice = "";
        priceLabel = "";
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<SaleColorSwatch> getColorSwatches() {
        return colorSwatches;
    }

    public void setColorSwatches(List<SaleColorSwatch> colorSwatches) {
        this.colorSwatches = colorSwatches;
    }

    public String getNowPrice() {
        return nowPrice;
    }

    public void setNowPrice(String nowPrice) {
        this.nowPrice = nowPrice;
    }

    public String getPriceLabel() {
        return priceLabel;
    }

    public void setPriceLabel(String priceLabel) {
        this.priceLabel = priceLabel;
    }

    public Double getPriceReduction() {
        return priceReduction;
    }

    public void setPriceReduction(Double priceReduction) {
        this.priceReduction = priceReduction;
    }
}
