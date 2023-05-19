package com.shpp.rstefanyshyn;

import org.apache.activemq.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class App implements Constant {


    private static final Logger logger = LoggerFactory.getLogger(App.class);


    public static void main(String[] args) throws Exception  {
            StopWatch stopWatch=new StopWatch();

            BDConnection bdConnection = new BDConnection();
            bdConnection.connect();
            ExecuteSqlFile executeSqlFile=new ExecuteSqlFile(bdConnection.getConnection());
            executeSqlFile.readDLL();
            stopWatch.restart();
            TableGen tableGen=new TableGen( bdConnection.getConnection());

            tableGen.reader(ADDRESS_VALUES,FILE_STORE);
            tableGen.reader(TYPE_VALUES,FILE_TYPE);
            stopWatch.stop();
            logger.info("Time of create table store and type {} ", (stopWatch.taken()) /THOUSAND);
            ProductGenerator productGenerator=
                    new ProductGenerator(bdConnection.getConnection());
            productGenerator.createProductStream();
            TableFinish tableFinish=new TableFinish( bdConnection.getConnection());
            logger.warn("store_address: {} , valid_products: {}, type_products: {}",
                    tableFinish.getRowCountFromStore(), tableFinish.getRowCountFromProduct(), productGenerator.getRowCountFromType());
            logger.info("Number rows in store_address: {}",(tableFinish.getRowCountFromStore()));
            logger.info("Number rows in valid_products: {}", (tableFinish.getRowCountFromProduct()));
            logger.info("Number rows in type_products: {}",(productGenerator.getRowCountFromType()));
            stopWatch.restart();
            tableFinish.generateFinishTable();
            stopWatch.stop();
            logger.info("Finish table create {} ", (stopWatch.taken()) /THOUSAND);
            stopWatch.restart();
            ProductFind productFind=new ProductFind(bdConnection.getConnection());
            productFind.find();
            stopWatch.stop();
            logger.info("find {} ", (stopWatch.taken()) /THOUSAND);

            bdConnection.disconnect();
        }

}
