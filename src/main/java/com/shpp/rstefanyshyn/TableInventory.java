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

    int batchSize = Integer.parseInt(BATH_SIZE);
    private static final Logger logger = LoggerFactory.getLogger(TableInventory.class);

    public void generateFinishTable() {
        String insertFinishQuery = "INSERT INTO inventory (store_id, product_id, qty) VALUES (?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertFinishQuery)) {
            Random randomForFinishTable = new Random();

            String selectStoresQuery = "SELECT id FROM store";
            String selectProductQuery = "SELECT id FROM products";
            PreparedStatement selectStoresStatement = connection.prepareStatement(selectStoresQuery);
            PreparedStatement selectProductsStatement = connection.prepareStatement(selectProductQuery);
            selectStoresStatement.execute();
            selectProductsStatement.execute();

            int rowCountProduct = getRowCountFromProduct();
            int rowCountStore = getRowCountFromStore();
            int rowInventory = rowCountProduct * rowCountStore;
            StopWatch stopWatch = new StopWatch();
            stopWatch.restart();
            IntStream.range(1, (rowCountProduct * rowCountStore + 1)).forEach(x -> {
                try {

                    int storeId = randomForFinishTable.nextInt(rowCountStore) + 1;
                    int productId = randomForFinishTable.nextInt(rowCountProduct) + 1;
                    int quantity = randomForFinishTable.nextInt(100) + 1;
                    preparedStatement.setInt(1, storeId);
                    preparedStatement.setInt(2, productId);
                    preparedStatement.setInt(3, quantity);
                    preparedStatement.addBatch();
                    if (x % batchSize == 0) {
                        preparedStatement.executeBatch();
                        preparedStatement.clearBatch();
                    }
                } catch (SQLException e) {
                    logger.warn("Insert products in stores fail!", e);
                }
            });
            stopWatch.stop();
            logger.warn("Create table inventory for second {} , Row in ... table : {},  RPS:{}", stopWatch.taken() / THOUSAND, rowInventory, 1000.0 * rowInventory / stopWatch.taken());
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
        String countQuery = "SELECT COUNT(*) AS row_count FROM inventory";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(countQuery);
            if (resultSet.next()) {
                return resultSet.getInt("row_count");
            }
        }
        return 0;
    }
}