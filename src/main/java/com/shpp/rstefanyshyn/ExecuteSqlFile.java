package com.shpp.rstefanyshyn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ExecuteSqlFile implements Constant{
    private final Logger logger = LoggerFactory.getLogger(ExecuteSqlFile.class);

    Connection connection;
    public ExecuteSqlFile(  Connection connection) throws SQLException {
        this.connection=connection;

    }
    public void readDLL() {
        try (//Connection connection = DriverManager.getConnection(SQL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             InputStream inputStream = ExecuteSqlFile.class.getClassLoader().getResourceAsStream(DDL_SQL)) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

                String line;
                StringBuilder sqlStatements = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    sqlStatements.append(line);


                    if (line.endsWith(";")) {
                        String sql = sqlStatements.toString();
                        statement.execute(sql);
                        sqlStatements.setLength(0);
                    }
                }
    logger.info("File Dll is completed {} ",DDL_SQL);

            }
        } catch (Exception e) {
            logger.error("File Dll is not completed {} ",DDL_SQL+ e);
        }
    }
}