package com.company.demo.util;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    private Constants() {
    }

    public static final Map<String, String> CURRENCY_SYMBOL = new HashMap<>() {
        {
            put("GBP", "£");
            put("USD", "$");
        }
    };

    public static final Map<String, String> COLOUR_CODES = new HashMap<>() {
        {
            put("Multi", "FF9933");// Assigning some random colour
            put("Green", "008000");
            put("Grey", "808080");
            put("Red", "FF0000");
            put("Blue", "0000FF");
            put("Pink", "FFC0CB");
            put("Yellow", "FFFF00");
            put("White", "FFFFFF");
            put("Black", "000000");
            put("Natural", "993366");// Assigning some random colour
            put("Orange", "FFA500");
        }
    };
}
