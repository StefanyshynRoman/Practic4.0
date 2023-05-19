package com.shpp.rstefanyshyn;


public interface Constant {
    GetProperty PROPERTY = new GetProperty("config.properties");
    GetProperty PROPERTY_CONNECTION = new GetProperty("connection.properties");

    String NUMBER_PRODUCTS = PROPERTY.getValueFromProperty("maximum");
    String FILE_STORE="adress_stores1.csv";
    String FILE_TYPE= "product_type.csv";
    String DDL_SQL="ddl.sql";
    String URL_SQL =PROPERTY_CONNECTION.getValueFromProperty("sql");
    String USER=PROPERTY_CONNECTION.getValueFromProperty("username.db");
    String PASSWORD=PROPERTY_CONNECTION.getValueFromProperty("password.db");

    String BATH_SIZE =PROPERTY.getValueFromProperty("bath_size") ;

    double THOUSAND =1000;

    String ADDRESS_VALUES = "INSERT INTO store ( address) VALUES (?)";
    String TYPE_VALUES = "INSERT INTO type ( type) VALUES (?)";
}
