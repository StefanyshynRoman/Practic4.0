package com.shpp.rstefanyshyn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TableGen implements Constant{
    private final Logger logger = LoggerFactory.getLogger(TableGen.class);
    Connection connection;

public TableGen(  Connection connection)  {
    this.connection=connection;

}
public void reader(String insertQuery, String file ) throws IOException, SQLException {
    InputStream inputStream = TableGen.class.getClassLoader().getResourceAsStream(file);
    assert inputStream != null;
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    logger.info("Read from {}",file);
    PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
    String line;
    while ((line = reader.readLine()) != null) {
        preparedStatement.setString(1, line.toLowerCase());
        preparedStatement.addBatch();
    }
    preparedStatement.executeBatch() ;
    preparedStatement.close();
    reader.close();

}

}
