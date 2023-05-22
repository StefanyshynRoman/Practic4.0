package com.shpp.rstefanyshyn;

import java.sql.*;
import java.util.Random;


public class TableFinish implements Constant {
    Connection connection;
    public TableFinish(Connection connection) {
        this.connection = connection;

    }


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
            int rowCount = getRowCountFromProduct();

            int count = 0;

                while (count < rowCount && productsResultSet.next() ) {
                int storeId = randomForFinishTable.nextInt(getRowCountFromStore())+1;
                int productId = productsResultSet.getInt("id");
                int quantity = randomForFinishTable.nextInt(100) + 1; // Random quantity between 1 and 100

                statement.setInt(1, storeId);
                statement.setInt(2, productId);
                statement.setInt(3, quantity);
                statement.addBatch();
                count++;
            }

            statement.executeBatch();
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
}