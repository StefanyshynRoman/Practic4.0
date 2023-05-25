package com.shpp.rstefanyshyn;


import jakarta.validation.constraints.*;

public class Product {

    @Pattern(regexp = "^(?!.*a).*$")
    String productName;

    int productTypeId;
    private boolean valid = true;

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Product(int typeId, String name) {
        this.productName = name;
        this.productTypeId = typeId;

    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(int productTypeId) {
        this.productTypeId = productTypeId;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name: " + '"' + productName + '"' +
                ", count: " + productTypeId +
                '}';

    }
}