package com.company.demo.util;

import java.util.Arrays;

public enum PriceLabelEnum {
    ShowWasNow("ShowWasNow"),
    ShowWasThenNow("ShowWasThenNow"),
    ShowPercDscount("ShowPercDscount");

    private String priceLabel;

    PriceLabelEnum(String partner) {
        this.priceLabel = partner;
    }

    public static PriceLabelEnum getPartner(final String label) {
        return Arrays.stream(values())
                .filter(e -> e.priceLabel.equals(label))
                .findFirst()
                .orElse(ShowWasNow);
    }
}
