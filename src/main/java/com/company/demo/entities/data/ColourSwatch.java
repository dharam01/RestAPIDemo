package com.company.demo.entities.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ColourSwatch {
    private String color;
    private String basicColor;
    private String skuId;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBasicColor() {
        return basicColor;
    }

    public void setBasicColor(String basicColor) {
        this.basicColor = basicColor;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }
}
