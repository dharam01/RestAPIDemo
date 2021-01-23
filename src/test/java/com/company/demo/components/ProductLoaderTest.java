package com.company.demo.components;

import com.company.demo.entities.data.ColourSwatch;
import com.company.demo.entities.data.Price;
import com.company.demo.entities.data.Product;
import com.company.demo.entities.data.ProductData;
import com.company.demo.entities.response.SaleColorSwatch;
import com.company.demo.entities.response.SaleProduct;
import com.company.demo.entities.response.SaleProductsData;
import com.company.demo.services.ProductService;
import com.company.demo.util.Constants;
import com.company.demo.util.PriceLabelEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductLoaderTest {

    private ProductLoader productLoader;
    @Mock
    private ProductService productService;
    @Mock
    private ProductData productData;

    @BeforeEach
    void setUp() {
        productService = mock(ProductService.class);
        productData = mock(ProductData.class);
        when(productData.getProducts()).thenReturn(createSaleProducts());
        when(productService.readFromFile()).thenReturn(productData);
        productLoader = new ProductLoader(productService);
    }

    @Test
    void testSaleProductsDataIsReturned() {
        SaleProductsData saleProductsData = productLoader.getSaleProducts(PriceLabelEnum.ShowWasNow.name());
        assertNotNull(saleProductsData);
    }

    @Test
    void testItShouldNotFailIfDataIsNotFoundInTheFile() {
        when(productData.getProducts()).thenReturn(new ArrayList<>());
        when(productService.readFromFile()).thenReturn(productData);
        SaleProductsData saleProductsData = productLoader.getSaleProducts(PriceLabelEnum.ShowWasNow.name());
        assertTrue(saleProductsData.getProducts().size() == 0);
    }

    @Test
    void testProductsOnSaleAreReturned() {
        SaleProductsData saleProductsData = productLoader.getSaleProducts(PriceLabelEnum.ShowPercDscount.name());
        List<SaleProduct> products = saleProductsData.getProducts();
        assertNotNull(products);
        assertTrue(products.size() > 0);
    }

    @Test
    void testProductIdAndTitle() {
        SaleProductsData saleProductsData = productLoader.getSaleProducts(PriceLabelEnum.ShowWasNow.name());
        SaleProduct saleProduct = saleProductsData.getProducts().get(0);
        assertEquals(saleProduct.getProductId(), "Product1");
        assertEquals(saleProduct.getTitle(), "Title Product 1");
    }

    @Test
    public void testColourSwatch() {
        SaleProductsData saleProductsData = productLoader.getSaleProducts(PriceLabelEnum.ShowWasNow.name());
        SaleProduct saleProduct = saleProductsData.getProducts().get(0);
        SaleColorSwatch saleColorSwatch = saleProduct.getColorSwatches().get(0);
        assertEquals(saleColorSwatch.getColor(), "Khaki");
        assertEquals(saleColorSwatch.getRgbColor(), Constants.COLOUR_CODES.get("Green"));
        assertEquals(saleColorSwatch.getColor(), "Khaki");
        assertEquals(saleColorSwatch.getSkuid(), "SKU1");
    }

    @Test
    public void testMultipleSwatches() {
        List<Product> products = createSaleProducts();
        ColourSwatch cs = new ColourSwatch();
        cs.setColor("Burgundy");
        cs.setBasicColor("Red");
        cs.setSkuId("SKU2");
        products.get(0).getColorSwatches().add(cs);
        when(productData.getProducts()).thenReturn(createSaleProducts());
        SaleProductsData saleProductsData = productLoader.getSaleProducts(PriceLabelEnum.ShowWasNow.name());
        assertTrue(saleProductsData.getProducts().get(0).getColorSwatches().size() > 0);
    }

    @Test
    public void testOnlyOnSaleProductsAreReturnedItShouldNotReturnProductThoseAreNotOnSale() {
        List<Product> testData = new ArrayList<>();
        testData.addAll(createSaleProducts());
        testData.add(createNonSaleProducts());
        when(productData.getProducts()).thenReturn(testData);
        when(productService.readFromFile()).thenReturn(productData);
        SaleProductsData saleProductsData = productLoader.getSaleProducts(PriceLabelEnum.ShowWasNow.name());
        assertTrue(saleProductsData.getProducts().size() == 1);
        assertEquals(saleProductsData.getProducts().get(0).getProductId(), "Product1");
    }

    @Test
    public void testProductAreGettingSortByReductionPriceWasPriceMinusNowPrice() {
        List<Product> testData = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("Product1");
        Price price1 = new Price();
        price1.setCurrency("GBP");
        price1.setNow("20.00");
        price1.setWas("40.00");
        product1.setPrice(price1);
        testData.add(product1);

        Product product2 = new Product();
        product2.setProductId("Product2");
        Price price2 = new Price();
        price2.setCurrency("GBP");
        price2.setNow("50.00");
        price2.setWas("90.00");
        product2.setPrice(price2);
        testData.add(product2);

        Product product3 = new Product();
        product3.setProductId("Product3");
        Price price3 = new Price();
        price3.setCurrency("GBP");
        price3.setNow("30.00");
        price3.setWas("60.00");
        product3.setPrice(price3);
        testData.add(product3);

        when(productData.getProducts()).thenReturn(testData);
        when(productService.readFromFile()).thenReturn(productData);
        SaleProductsData saleProductsData = productLoader.getSaleProducts(PriceLabelEnum.ShowWasNow.name());
        List<SaleProduct> saleProducts = saleProductsData.getProducts();
        assertTrue(saleProducts.size() == 3);
        assertEquals(saleProducts.get(0).getProductId(), "Product2");
        assertEquals(saleProducts.get(1).getProductId(), "Product3");
        assertEquals(saleProducts.get(2).getProductId(), "Product1");
    }

    @Test
    public void testWhenPriceLabelParameterIsShowWasNow() {
        SaleProductsData saleProductsData = productLoader.getSaleProducts(PriceLabelEnum.ShowWasNow.name());
        SaleProduct saleProduct = saleProductsData.getProducts().get(0);
        assertEquals(saleProduct.getPriceLabel(), "Was £40, now £20");
    }

    @Test
    public void testWhenPriceLabelParameterIsShowWasThenNow() {
        SaleProductsData saleProductsData = productLoader.getSaleProducts(PriceLabelEnum.ShowWasThenNow.name());
        SaleProduct saleProduct = saleProductsData.getProducts().get(0);
        assertEquals(saleProduct.getPriceLabel(), "Was £40, then £30, now £20");
    }

    @Test
    public void testWhenPriceLabelParameterIsShowPercDscount() {
        SaleProductsData saleProductsData = productLoader.getSaleProducts(PriceLabelEnum.ShowPercDscount.name());
        SaleProduct saleProduct = saleProductsData.getProducts().get(0);
        assertEquals(saleProduct.getPriceLabel(), "50% off - now £20");
    }

    @Test
    public void testWhenPriceLabelParameterIsShowWasThenNowAndThen2IsEmptyDisplayThen1Price() {
        List<Product> testData = new ArrayList<>();
        Product product = new Product();
        product.setProductId("Product1");
        Price price = new Price();
        price.setCurrency("GBP");
        price.setNow("20.00");
        price.setWas("40.00");
        price.setThen("30.00");
        price.setThen1("25.00");
        price.setThen2("");
        product.setPrice(price);
        testData.add(product);
        when(productData.getProducts()).thenReturn(testData);
        when(productService.readFromFile()).thenReturn(productData);
        SaleProductsData saleProductsData = productLoader.getSaleProducts(PriceLabelEnum.ShowWasThenNow.name());
        SaleProduct saleProduct = saleProductsData.getProducts().get(0);
        assertEquals(saleProduct.getPriceLabel(), "Was £40, then £25, now £20");
    }

    @Test
    public void testWhenPriceLabelParameterIsShowWasThenNowAndThen2AndThen1AreEmptyDoNotDisplayThenPrice() {
        List<Product> testData = new ArrayList<>();
        Product product = new Product();
        product.setProductId("Product1");
        Price price = new Price();
        price.setCurrency("GBP");
        price.setNow("20.00");
        price.setWas("40.00");
        price.setThen("30.00");
        price.setThen1("");
        price.setThen2("");
        product.setPrice(price);
        testData.add(product);
        when(productData.getProducts()).thenReturn(testData);
        when(productService.readFromFile()).thenReturn(productData);
        SaleProductsData saleProductsData = productLoader.getSaleProducts(PriceLabelEnum.ShowWasThenNow.name());
        SaleProduct saleProduct = saleProductsData.getProducts().get(0);
        assertEquals(saleProduct.getPriceLabel(), "Was £40, now £20");
    }

    @Test
    public void testWhenPriceLabelParameterIsShowWasThenNowAndThen2PresentButThenIsEmptyDoNotDisplayThenPrice() {
        List<Product> testData = new ArrayList<>();
        Product product = new Product();
        product.setProductId("Product1");
        Price price = new Price();
        price.setCurrency("GBP");
        price.setNow("20.00");
        price.setWas("40.00");
        price.setThen("30.00");
        price.setThen1("");
        price.setThen2("");
        product.setPrice(price);
        testData.add(product);
        when(productData.getProducts()).thenReturn(testData);
        when(productService.readFromFile()).thenReturn(productData);
        SaleProductsData saleProductsData = productLoader.getSaleProducts(PriceLabelEnum.ShowWasThenNow.name());
        SaleProduct saleProduct = saleProductsData.getProducts().get(0);
        assertEquals(saleProduct.getPriceLabel(), "Was £40, now £20");
    }

    @Test
    public void testIntegerPriceLessThanTenItShouldBeDisplayedAsDecimal() {
        List<Product> testData = new ArrayList<>();
        Product product = new Product();
        product.setProductId("Product1");
        Price price = new Price();
        price.setCurrency("GBP");
        price.setNow("8");
        price.setWas("15.00");
        price.setThen("");
        price.setThen1("");
        price.setThen2("");
        product.setPrice(price);
        testData.add(product);
        when(productData.getProducts()).thenReturn(testData);
        when(productService.readFromFile()).thenReturn(productData);
        SaleProductsData saleProductsData = productLoader.getSaleProducts(PriceLabelEnum.ShowWasNow.name());
        SaleProduct saleProduct = saleProductsData.getProducts().get(0);
        assertEquals(saleProduct.getPriceLabel(), "Was £15, now £8.00");
    }

    @Test
    public void testPriceDoublePriceMoreThanTenWithDecimalPartAsZeroItShouldBeDisplayedAsInteger() {
        List<Product> testData = new ArrayList<>();
        Product product = new Product();
        product.setProductId("Product1");
        Price price = new Price();
        price.setCurrency("GBP");
        price.setNow("85.00");
        price.setWas("120.99");
        price.setThen("");
        price.setThen1("");
        price.setThen2("");
        product.setPrice(price);
        testData.add(product);
        when(productData.getProducts()).thenReturn(testData);
        when(productService.readFromFile()).thenReturn(productData);
        SaleProductsData saleProductsData = productLoader.getSaleProducts(PriceLabelEnum.ShowWasNow.name());
        SaleProduct saleProduct = saleProductsData.getProducts().get(0);
        assertEquals(saleProduct.getPriceLabel(), "Was £120.99, now £85");
    }

    private List<Product> createSaleProducts() {
        List<Product> data = new ArrayList<>();
        Product product = new Product();
        product.setProductId("Product1");
        product.setTitle("Title Product 1");
        Price price = new Price();
        price.setCurrency("GBP");
        price.setNow("20.00");
        price.setWas("40.00");
        price.setThen("30.00");
        price.setThen1("25.00");
        price.setThen2("20.00");
        product.setPrice(price);
        ColourSwatch cs = new ColourSwatch();
        cs.setColor("Khaki");
        cs.setBasicColor("Green");
        cs.setSkuId("SKU1");
        List<ColourSwatch> swatches = new ArrayList<>();
        swatches.add(cs);
        product.setColorSwatches(swatches);
        data.add(product);
        return data;
    }

    private Product createNonSaleProducts() {
        Product product = new Product();
        product.setProductId("NS-Product");
        product.setTitle("Title NS-Product");
        Price price = new Price();
        price.setCurrency("GBP");
        price.setNow("50.00");
        price.setWas("");
        price.setThen("");
        price.setThen1("");
        price.setThen2("");
        product.setPrice(price);
        ColourSwatch cs = new ColourSwatch();
        cs.setColor("NS-Khaki");
        cs.setBasicColor("Burgundy");
        cs.setSkuId("NS-SKU");
        List<ColourSwatch> swatches = new ArrayList<>();
        swatches.add(cs);
        product.setColorSwatches(swatches);
        return product;
    }
}