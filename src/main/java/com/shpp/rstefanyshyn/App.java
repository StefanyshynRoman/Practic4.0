package com.shpp.rstefanyshyn;

import org.apache.activemq.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
 */

    private static final Logger logger = LoggerFactory.getLogger(App.class);


    public static void main(String[] args) throws Exception  {
            StopWatch stopWatch=new StopWatch();
            BDConnection bdConnection = new BDConnection();
            bdConnection.disconnect();
            bdConnection.connect();
            ExecuteSqlFile executeSqlFile=new ExecuteSqlFile(bdConnection.getConnection());
            executeSqlFile.readDLL();
            stopWatch.restart();
            TableGen tableGen=new TableGen( bdConnection.getConnection());

            tableGen.reader(ADDRESS_VALUES,FILE_STORE);
            tableGen.reader(TYPE_VALUES,FILE_TYPE);
            stopWatch.stop();
            logger.info("Time (second) of create table store and type {} ", (stopWatch.taken()) /THOUSAND);
            ProductGenerator productGenerator=
                    new ProductGenerator(bdConnection.getConnection());
            productGenerator.createProductStream();
            TableFinish tableFinish=new TableFinish( bdConnection.getConnection());
            logger.warn("store_address: {} , valid_products: {}, type_products: {}",
                    tableFinish.getRowCountFromStore(), tableFinish.getRowCountFromProduct(), productGenerator.getRowCountFromType());
            stopWatch.restart();
            tableFinish.generateFinishTable();
            stopWatch.stop();
            logger.info("Finish table create {} ", (stopWatch.taken()) /THOUSAND);
            stopWatch.restart();
            ProductFind productFind=new ProductFind(bdConnection.getConnection());
            productFind.find();
            stopWatch.stop();
            logger.info("Find  products type , seconds {} ", (stopWatch.taken()) /THOUSAND);

            bdConnection.disconnect();
        }

}
