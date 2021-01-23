package com.company.demo.components;

import com.company.demo.entities.data.ColourSwatch;
import com.company.demo.entities.data.Price;
import com.company.demo.entities.data.Product;
import com.company.demo.entities.response.SaleColorSwatch;
import com.company.demo.entities.response.SaleProduct;
import com.company.demo.entities.response.SaleProductsData;
import com.company.demo.services.ProductService;
import com.company.demo.util.PriceLabelEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.company.demo.util.Constants.COLOUR_CODES;
import static com.company.demo.util.Constants.CURRENCY_SYMBOL;

/**
 * This a component it call the {@link ProductService} to get the products data.
 */
@Component
public class ProductLoader {
    private final static Logger LOGGER = LoggerFactory.getLogger(ProductLoader.class.getName());
    private final static DecimalFormat DECIMAL_FMT_TWO = new DecimalFormat("#.00");
    private final static DecimalFormat DECIMAL_FMT_ZERO = new DecimalFormat("#");

    private final ProductService productService;

    public ProductLoader(final ProductService productService) {
        this.productService = productService;
    }

    /**
     * Returns sales products.
     *
     * @return
     */
    public SaleProductsData getSaleProducts(final String labelType) {
        LOGGER.debug("Get all the products from jason file.");
        List<Product> allProducts = productService.readFromFile().getProducts();
        LOGGER.debug("Found total {} products", allProducts.size());
        // Filter Sale products
        List<SaleProduct> saleProducts = allProducts
                .stream()
                .filter(p -> ((p.getPrice().getWas() != null && p.getPrice().getWas() != "")
                        && (p.getPrice().getNow() != null && p.getPrice().getNow() != ""))
                        && (Double.valueOf(p.getPrice().getWas()).compareTo(Double.valueOf(p.getPrice().getNow())) > 0.0))
                .map(sp -> decorateProduct(sp, labelType))
                .sorted(Comparator.comparingDouble(SaleProduct::getPriceReduction)
                        .reversed())
                .collect(Collectors.toList());

        SaleProductsData saleProductsData = new SaleProductsData();
        saleProductsData.setProducts(saleProducts);
        return saleProductsData;
    }

    /**
     * Decorates product data to be sent in response.
     *
     * @param p
     * @param labelType
     * @return
     */
    private SaleProduct decorateProduct(final Product p, final String labelType) {
        LOGGER.debug("Decorating the product {}", p.getProductId());
        SaleProduct saleProduct = new SaleProduct();
        saleProduct.setProductId(p.getProductId());
        saleProduct.setTitle(p.getTitle());
        decoratePrice(saleProduct, p.getPrice(), labelType);
        saleProduct.setColorSwatches(decorateSwatches(p.getColorSwatches()));
        LOGGER.debug("Product has been decorated successfully");
        return saleProduct;
    }

    /**
     * Decorates the price information.
     *
     * @param saleProduct
     * @param price
     * @param labelType
     */
    private void decoratePrice(SaleProduct saleProduct, Price price, String labelType) {
        LOGGER.debug("Decorating price information form product {}", saleProduct.getProductId());
        Double dNow = Double.valueOf(price.getNow());
        Double dWas = Double.valueOf(price.getWas());
        saleProduct.setPriceReduction(dWas - dNow);
        saleProduct.setNowPrice(formatPrice(dNow, price.getCurrency()));
        saleProduct.setPriceLabel(formatPriceLabel(labelType, saleProduct, price));
    }

    /**
     * Formats the price adds currency into it.
     *
     * @param value
     * @param currency
     * @return
     */
    private String formatPrice(final Double value, final String currency) {
        LOGGER.debug("Formatting the price {} currency {}", value, currency);
        if (value < 10) {
            return CURRENCY_SYMBOL.getOrDefault(currency, "") + DECIMAL_FMT_TWO.format(value);
        } else {
            if (value == value.intValue()) {
                return CURRENCY_SYMBOL.getOrDefault(currency, "") + DECIMAL_FMT_ZERO.format(value);
            } else {
                return CURRENCY_SYMBOL.getOrDefault(currency, "") + DECIMAL_FMT_TWO.format(value);
            }
        }
    }

    /**
     * Formats the price label it takes the request parameter <b>labelType<b/> and populates the labels accordingly.
     *
     * @param labelType
     * @param saleProduct
     * @param price
     * @return
     */
    private String formatPriceLabel(final String labelType, final SaleProduct saleProduct, final Price price) {
        LOGGER.debug("Formatting price label for product {} and label type {}", saleProduct.getProductId(), labelType);
        PriceLabelEnum priceLabelEnum = PriceLabelEnum.getPartner(labelType);
        String result = "";
        String strWAs = formatPrice(Double.valueOf(price.getWas()), price.getCurrency());
        switch (priceLabelEnum) {
            case ShowWasNow:
                result = "Was " + strWAs + ", now " + saleProduct.getNowPrice();
                break;
            case ShowWasThenNow:
                String then = "";
                if (price.getThen2() != null && !price.getThen2().isBlank()) {
                    if (price.getThen() != null && !price.getThen().isBlank()) {
                        then = price.getThen();
                    }
                } else if (price.getThen1() != null && !price.getThen1().isBlank()) {
                    then = price.getThen1();
                }
                result = "Was " + strWAs;
                if (!then.isBlank()) {
                    String strThen = formatPrice(Double.valueOf(then), price.getCurrency());
                    result = result + ", then " + strThen;
                }
                result = result + ", now " + saleProduct.getNowPrice();
                break;
            case ShowPercDscount:
                Double dWas = Double.valueOf(price.getWas());
                Double dNow = Double.valueOf(price.getNow());
                Double discount = ((dWas - dNow) / dWas) * 100;
                result = discount.intValue() + "% off - now " + saleProduct.getNowPrice();
                break;
            default:
                throw new IllegalStateException("Price label is not supported" + labelType);
        }
        return result;
    }

    /**
     * Populates the colour swatches.
     *
     * @param dSwatches
     * @return
     */
    private List<SaleColorSwatch> decorateSwatches(List<ColourSwatch> dSwatches) {
        LOGGER.debug("Decorating swatches.");
        List<SaleColorSwatch> swatches = new ArrayList<>();
        if (dSwatches != null && dSwatches.size() > 0) {
            dSwatches.forEach(s -> {
                SaleColorSwatch swatch = new SaleColorSwatch();
                swatch.setSkuid(s.getSkuId());
                swatch.setColor(s.getColor());
                swatch.setRgbColor(COLOUR_CODES.getOrDefault(s.getBasicColor(), ""));
                swatches.add(swatch);
            });
        }
        return swatches;
    }
}
