package com.shpp.rstefanyshyn;


import jakarta.validation.constraints.*;
public class Product {

//   @Size(min = 6, message = "Name must be more than 9 characters") //medium
//    regexp = "^(?!.*a).*$"
    @Pattern(regexp = "^(?!.*a).*$")
    String productName;

    int productTypeId;
    @Min(2)
    //@Max(100)
 //   double price;



    public Product(int typeId , String name ) {
        this.productName = name;
        this.productTypeId = typeId;
       // this.price=price;

    }
//   // public double getPrice() {
//        return price;
//    }

//    public void setPrice(double price) {
//        this.price = price;
//    }
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
              //  ", price: " + price +
                '}';

    }
}