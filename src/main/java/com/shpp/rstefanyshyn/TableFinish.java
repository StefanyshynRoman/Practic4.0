package com.shpp.rstefanyshyn;

import org.apache.activemq.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Random;
import java.util.stream.IntStream;


public class TableFinish implements Constant {
    Connection connection;

    public TableFinish(Connection connection) {
        this.connection = connection;

    }

    int batchSize = Integer.parseInt(BATH_SIZE);
    private static final Logger logger = LoggerFactory.getLogger(TableFinish.class);

    public void generateFinishTable() {
        String insertFinishQuery = "INSERT INTO finish (store_id, product_id, qty) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(insertFinishQuery)) {
            Random randomForFinishTable = new Random();

            String selectStoresQuery = "SELECT id FROM store";
            String selectProductQuery = "SELECT id FROM products";
            PreparedStatement selectStoresStatement = connection.prepareStatement(selectStoresQuery);
            PreparedStatement selectProductsStatement = connection.prepareStatement(selectProductQuery);
            selectStoresStatement.execute();
            selectProductsStatement.execute();
            ResultSet productsResultSet = selectProductsStatement.getResultSet();
            int rowCountProduct = getRowCountFromProduct();
            int rowCountStore = getRowCountFromStore();
            int rowInventory = rowCountProduct * rowCountStore;
            int quantity = randomForFinishTable.nextInt(100) + 1;
            StopWatch stopWatch = new StopWatch();
            stopWatch.restart();
            IntStream.range(1, (rowCountProduct * rowCountStore + 1)).forEach(x -> {
                try {
                    int storeId = randomForFinishTable.nextInt(rowCountStore) + 1;
                    int productId = randomForFinishTable.nextInt(rowCountProduct) + 1;
                    statement.setInt(1, storeId);
                    statement.setInt(2, productId);
                    statement.setInt(3, quantity);
                    statement.addBatch();
                    if (x % batchSize == 0) {
                        statement.executeBatch();
                        //  statement.getConnection().commit();
                        //  logger.info("Batch add {}", x);
                        statement.clearBatch();
                    }
                } catch (SQLException e) {
                    logger.warn("Insert products in stores fail!", e);
                }
            });
            stopWatch.stop();
            logger.warn("Create table inventory for second {} , Row in ... table {}: {},  RPS:{}", stopWatch.taken() / THOUSAND, rowInventory, 1000.0 * rowInventory / stopWatch.taken());

//                while (count < rowCountProduct  ) {
//                int storeId = randomForFinishTable.nextInt(getRowCountFromStore())+1;
//                int productId = randomForFinishTable.nextInt(getRowCountFromProduct())+1;
//                int quantity = randomForFinishTable.nextInt(100) + 1; // Random quantity between 1 and 100
//
//                statement.setInt(1, storeId);
//                statement.setInt(2, productId);
//                statement.setInt(3, quantity);
//                statement.addBatch();
//                count++;
//            }
//
//            statement.executeBatch();
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getRowCountFromProduct() throws SQLException {
        String countQuery = "SELECT COUNT(*) AS row_count FROM products";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(countQuery);
            if (resultSet.next()) {
                return resultSet.getInt("row_count");
            }
        }
        return 0;
    }

    public int getRowCountFromStore() throws SQLException {
        String countQuery = "SELECT COUNT(*) AS row_count FROM store";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(countQuery);
            if (resultSet.next()) {
                return resultSet.getInt("row_count");
            }
        }
        return 0;
    }

    public int getRowCountFromInventory() throws SQLException {
        String countQuery = "SELECT COUNT(*) AS row_count FROM finish";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(countQuery);
            if (resultSet.next()) {
                return resultSet.getInt("row_count");
            }
        }
        return 0;
    }
}