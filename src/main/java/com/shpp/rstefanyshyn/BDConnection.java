package com.shpp.rstefanyshyn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BDConnection implements Constant {
    private final Logger logger = LoggerFactory.getLogger(BDConnection.class);
    private Connection connection;

    public boolean connect() throws SQLException {
        connection = DriverManager.getConnection(
                URL_SQL,USER,PASSWORD    );
        connection.setAutoCommit(true);
        connection.createStatement();
        logger.info("Connected to the database.");
        return true;
    }

    public boolean disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            logger.info("Disconnected from the database.");

        }
        return false;
    }

    public Connection getConnection() {
        return connection;
    }
}