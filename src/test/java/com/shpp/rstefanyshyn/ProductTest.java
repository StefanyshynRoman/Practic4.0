package com.shpp.rstefanyshyn;
import org.junit.Test;
import static org.junit.Assert.*;
public class ProductTest {
    @Test
    public void testGettersAndSetters() {
        int typeId = 1;
        String name = "Test Product";
        Product product = new Product(typeId, name);
        int actualTypeId = product.getProductTypeId();
        String actualName = product.getProductName();
        assertEquals(typeId, actualTypeId);
        assertEquals(name, actualName);
    }

    @Test
    public void testToString() {
        int typeId = 1;
        String name = "Test Product";
        Product product = new Product(typeId, name);
        String actualToString = product.toString();
        String expectedToString = "Product{name: \"Test Product\", count: 1}";
        assertEquals(expectedToString, actualToString);
    }
}
