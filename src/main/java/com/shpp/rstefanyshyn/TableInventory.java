package com.shpp.rstefanyshyn;

import org.apache.activemq.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Random;
import java.util.stream.IntStream;


public class TableInventory implements Constant {
    Connection connection;

    public TableInventory(Connection connection) {
        this.connection = connection;

    }

    int batchSize = Integer.parseInt(BATCH_SIZE);
    private static final Logger logger = LoggerFactory.getLogger(TableInventory.class);

    public void generateFinishTable() throws SQLException {

        String insertFinishQuery = "INSERT INTO inventory (store_id, product_id, qty) VALUES (?, ?, ?)";
        String selectStoresQuery = "SELECT id FROM store";
        String selectProductQuery = "SELECT id FROM products";
        try (PreparedStatement insertPreparedStatement = connection.prepareStatement(insertFinishQuery);
             PreparedStatement selectStoresStatement = connection.prepareStatement(selectStoresQuery);
             PreparedStatement selectProductsStatement = connection.prepareStatement(selectProductQuery);) {
            Random randomForFinishTable = new Random();
            connection.setAutoCommit(false);
            selectStoresStatement.execute();
            selectProductsStatement.execute();

            int rowCountProduct = getRowCountFromProduct();
            int rowCountStore = getRowCountFromStore();
            int rowInventory = rowCountProduct * rowCountStore;
            StopWatch stopWatch = new StopWatch();
            stopWatch.restart();
            IntStream.range(1, (rowCountProduct * rowCountStore + 1)).forEach(x -> {
                try {
                    if ((x % 100_000) == 0) {
                        logger.info("Products added: {}", x);
                    }

                    int storeId = randomForFinishTable.nextInt(rowCountStore) + 1;
                    int productId = randomForFinishTable.nextInt(rowCountProduct) + 1;
                    int quantity = randomForFinishTable.nextInt(100) + 1;
                    insertPreparedStatement.setInt(1, storeId);
                    insertPreparedStatement.setInt(2, productId);
                    insertPreparedStatement.setInt(3, quantity);
                    insertPreparedStatement.addBatch();
                    if (x % batchSize == 0) {
                        insertPreparedStatement.executeBatch();
                        insertPreparedStatement.clearBatch();
                    }

                } catch (SQLException e) {
                    logger.warn("Insert products in stores fail!", e);
                }
            });

            insertPreparedStatement.executeBatch();
            insertPreparedStatement.clearBatch();
            stopWatch.stop();
            logger.warn("Create table inventory for second {} , Row in ... table : {},  RPS:{}", stopWatch.taken() / THOUSAND, rowInventory, 1000.0 * rowInventory / stopWatch.taken());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            connection.setAutoCommit(true);
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
}