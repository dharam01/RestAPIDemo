package com.company.demo.services;

import com.company.demo.entities.data.ProductData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This is service class to load the product data from json file
 */
@Service
public class ProductService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ProductService.class.getName());
    private static ProductData PRODUCT_DATA = new ProductData();

    public ProductService() {
        loadProductData();
    }

    /**
     * Returns the product data.
     *
     * @return
     */
    public ProductData readFromFile() {
        if (PRODUCT_DATA == null) {
            LOGGER.debug("Data is not loaded yet going to load product data from json file.");
            loadProductData();
        }
        return PRODUCT_DATA;
    }

    private synchronized void loadProductData() {
        LOGGER.debug("Loading product data from json file.");
        String content = null;
        try {
            String filePath = ResourceUtils.getFile("classpath:static/productData.json").getAbsolutePath();
            LOGGER.debug("File Path:" + filePath);
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            LOGGER.error("There was issue while reading product data from static/productData.json file." + e.getMessage());
        }
        if (content != null && content.length() > 0) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                PRODUCT_DATA = mapper.readValue(content, ProductData.class);
            } catch (JsonProcessingException e) {
                LOGGER.error("Can not parse json file." + e.getMessage());
            }
        }
    }
}
