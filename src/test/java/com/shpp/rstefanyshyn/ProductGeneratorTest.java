package com.shpp.rstefanyshyn;

import jakarta.validation.Validator;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import junit.framework.TestCase;

public class ProductGeneratorTest extends TestCase implements Constant {
    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock

    private Statement mockStatement;

    @Mock
    private ResultSet mockResultSet;



    private ProductGenerator productGenerator;




    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        productGenerator = new ProductGenerator(mockConnection);
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
    }


    @Test
    public  void testGenerateType() throws SQLException {
        int rowCount = 10;
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("row_count")).thenReturn(rowCount);
        int productType = productGenerator.generateType();
        assertEquals(rowCount, 10);
        assertNotEquals(11, productType);

    }
    @Test
    public   void testGenerateRandomProductName() throws SQLException {
        Product product = new Product(1, "Test");
        int rowCount = 10;
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("row_count")).thenReturn(rowCount);
        int productType = productGenerator.generateType();
        assertNotEquals(product.getProductName(), productGenerator.generateRandomProductName());
        assertNotEquals(11, productType);

    }
}



