package com.shpp.rstefanyshyn;

import org.apache.activemq.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;


public class App implements Constant {
/*
100_000 max
RPS - 5292.96564865294, Bach size - 100
RPS - 5313.778627982359, Bach size - 100
RPS - 6668.889629876626, Bach size - 500
RPS - 6694.336591243808, Bach size - 500
RPS - 6881.365262868153, Bach size - 1000
RPS - 6794.863083508867, Bach size - 1000
RPS - 6127.075546841493, Bach size - 2000
RPS - 6394.2707334228535, Bach size - 2000
RPS - 6709.1580006709155, Bach size - 10000
RPS - 6572.461386789352, Bach size - 10000
 1_000_000
RPS - 6715.420620370557, Bach size - 500
RPS - 7447.0144918902015, Bach size - 1000
RPS - 6981.631327976095, Bach size - 2000
RPS - 7448.179292571931, Bach size - 10000

c.s.rstefanyshyn.App Time (second) of create table store and type 0.077
INFO  o.h.v.i.util.Version HV000001: Hibernate Validator 8.0.0.Final
INFO  c.s.r.ProductGenerator Generation products: 100000
INFO  c.s.r.ProductGenerator Generation products is over: seconds - 17.251
WARN  c.s.r.ProductGenerator RPS - 5796.765404904064, Batch size - 1000
WARN  c.s.r.ProductGenerator Product invalid: 12033
WARN  c.s.rstefanyshyn.App store_address: 30 , valid_products: 100000, type_products: 102
WARN  c.s.r.TableFinish Create table inventory for second 120.893 , Row in ... table : 3000000,  RPS:24815.332566815283WARN  c.s.r.ProductFind Store's address  with test: "sumy, kharkivska street, 63"
INFO  c.s.rstefanyshyn.App Find  products type , seconds 1.228

INFO  o.h.v.i.util.Version HV000001: Hibernate Validator 8.0.0.Final
INFO  c.s.r.ProductGenerator Generation products: 100000
INFO  c.s.r.ProductGenerator Generation products is over: seconds - 20.387
WARN  c.s.r.ProductGenerator RPS - 4905.086574778044, Batch size - 1000
WARN  c.s.r.ProductGenerator Product invalid: 12016
WARN  c.s.rstefanyshyn.App store_address: 30 , valid_products: 100000, type_products: 102
WARN  c.s.r.TableFinish Create table inventory for second 125.968 , Row in ... table : 3000000,  RPS:23815.572208814938
WARN  c.s.r.ProductFind Store's address  with test: "lutsk, volodymyra velykoho street, 4"
INFO  c.s.rstefanyshyn.App Find  products type , seconds 3.773
INFO  c.s.r.BDConnection Disconnected from the database.

INFO  c.s.rstefanyshyn.App Time (second) of create table store and type 0.096
INFO  o.h.v.i.util.Version HV000001: Hibernate Validator 8.0.0.Final
INFO  c.s.r.ProductGenerator Generation products: 100000
INFO  c.s.r.ProductGenerator Generation products is over: seconds - 17.913
WARN  c.s.r.ProductGenerator RPS - 5582.5378216937415, Batch size - 1000
WARN  c.s.r.ProductGenerator Product invalid: 11894
WARN  c.s.rstefanyshyn.App store_address: 30 , valid_products: 100000, type_products: 102
WARN  c.s.r.TableFinish Create table inventory for second 140.891 , Row in ... table : 3000000,  RPS:21293.056334329376
WARN  c.s.r.ProductFind Store's address  with test: "kamianets-podilskyi, vyacheslava chornovola street, 59"
INFO  c.s.rstefanyshyn.App Find  products type , seconds 3.997
INFO  c.s.r.BDConnection Disconnected from the database.
CREATE INDEX idx1 ON products (type_id);
CREATE INDEX idx10 ON inventory (product_id);
CREATE INDEX idx2 ON inventory (store_id);

 */

    static BDConnection bdConnection = new BDConnection();
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    static StopWatch stopWatch = new StopWatch();
    static ProductGenerator productGenerator;
    //static ProductGenerator1 productGenerator;

    public static void main(String[] args) throws Exception {

        // BDConnection bdConnection = new BDConnection();
        connectToBD();
        readDLL();
        genHelpTable();
        generateProduct();
        generateInventoryTable();
        findProduct();
        disconnect();
    }

    private static void disconnect() throws SQLException {
        bdConnection.disconnect();
    }

    private static void findProduct() {
        stopWatch.restart();
        ProductFind productFind = new ProductFind(bdConnection.getConnection());
        productFind.find();
        stopWatch.stop();
        logger.info("Find  products type , seconds {} ", (stopWatch.taken()) / THOUSAND);
    }

    private static void generateInventoryTable() throws SQLException {
        TableInventory tableInventory = new TableInventory(bdConnection.getConnection());
        logger.warn("store_address: {} , products: {}, type_products: {}",
                tableInventory.getRowCountFromStore(), tableInventory.getRowCountFromProduct(), productGenerator.getRowCountFromType());
        tableInventory.generateFinishTable();


    }


    private static void connectToBD() throws SQLException {
        bdConnection.disconnect();
        bdConnection.connect();
    }

    private static void readDLL() {
        ExecuteSqlFile executeSqlFile = new ExecuteSqlFile(bdConnection.getConnection());
        executeSqlFile.readDLL();
    }

    private static void genHelpTable() throws SQLException, IOException {
        stopWatch.restart();
        TableGen tableGen = new TableGen(bdConnection.getConnection());

        tableGen.reader(ADDRESS_VALUES, FILE_STORE);
        tableGen.reader(TYPE_VALUES, FILE_TYPE);
        stopWatch.stop();
        logger.info("Time (second) of create table store and type {} ", (stopWatch.taken()) / THOUSAND);

    }

    private static void generateProduct() {
        productGenerator =
                new ProductGenerator(bdConnection.getConnection());
        productGenerator.createProductStream();
    }


}
