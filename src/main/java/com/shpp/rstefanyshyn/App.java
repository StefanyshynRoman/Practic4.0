package com.shpp.rstefanyshyn;

import org.apache.activemq.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;


public class App implements Constant {

    static BDConnection bdConnection = new BDConnection();
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    static StopWatch stopWatch = new StopWatch();
    static ProductGenerator productGenerator;


    public static void main(String[] args) throws Exception {


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
