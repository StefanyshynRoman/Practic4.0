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
        generatFinishTable();
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

    private static void generatFinishTable() throws SQLException {
        TableFinish tableFinish = new TableFinish(bdConnection.getConnection());
        logger.warn("store_address: {} , valid_products: {}, type_products: {}",
                tableFinish.getRowCountFromStore(), tableFinish.getRowCountFromProduct(), productGenerator.getRowCountFromType());

       // stopWatch.restart();
        tableFinish.generateFinishTable();
     //   stopWatch.stop();
//        logger.info("Finish table create {} ", (stopWatch.taken()) / THOUSAND);
//        logger.info("Row in ... table {} ",tableFinish.getRowCountFromInventory());
        //logger.warn("Create table inventory for second {} , Row in ... table {}: {},  RPS:{}", stopWatch.taken()/ THOUSAND,rowInventory, 1000.0 * rowInventory / stopWatch.taken());

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
                productGenerator=
                        new ProductGenerator(bdConnection.getConnection());
                productGenerator.createProductStream();
        }
//    private static void generateProduct() {
//        productGenerator =
//                new ProductGenerator1(bdConnection.getConnection());
//        productGenerator.createProductStream();
//    }

}
