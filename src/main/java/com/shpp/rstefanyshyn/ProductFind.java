package com.shpp.rstefanyshyn;

import org.apache.activemq.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ProductFind implements Constant{
    private final Logger logger = LoggerFactory.getLogger(ProductFind.class);
    Connection connection;

    public ProductFind(Connection connection)  {
        this.connection = connection;

    }
    public void find() {
        StopWatch stopWatch=new StopWatch();
        stopWatch.restart();

           String productType = System.getProperty("type","test").toLowerCase();
            String sql =  "SELECT s.address " +
                    "FROM store s " +
                    "JOIN finish f ON s.id = f.store_id " +
                    "JOIN products p ON f.product_id = p.id " +
                    "JOIN type t ON p.type_id = t.id " +
                    "WHERE t.type = ? " +
                    "GROUP BY s.id " +
                    "ORDER BY SUM(f.qty) DESC " +
                    "LIMIT 1";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, productType);
                ResultSet resultSet = statement.executeQuery();


                if (resultSet.next()) {
                    String storeAddress = resultSet.getString("address");
                    logger.warn("Store's address  with " + productType + ": " + storeAddress);

                } else {
                    logger.warn("Store's not found with type : " + productType );
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }  stopWatch.stop();



    }
}