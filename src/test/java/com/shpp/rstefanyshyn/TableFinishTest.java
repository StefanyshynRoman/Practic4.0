package com.shpp.rstefanyshyn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TableFinishTest {
   // @Mock
    //private Connection mockConnection;


    @Mock
    ResultSet resultSet;
//    @Mock
//    private Statement mockStatement;
//    @Mock
//    private ResultSet mockResultSet;
PreparedStatement mockPreparedStatement=mock(PreparedStatement.class);
    Connection mockConnection = mock(Connection.class);
    Statement mockStatement = mock(Statement.class);
    ResultSet mockResultSet = mock(ResultSet.class);
    TableInventory tableInventory = new TableInventory(mockConnection);
   // private TableFinish tableFinish;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
//        Connection mockConnection = mock(Connection.class);
//        Statement mockStatement = mock(Statement.class);
//        ResultSet mockResultSet = mock(ResultSet.class);

        // Create an instance of TableFinish with the mock connection
        TableInventory tableInventory = new TableInventory(mockConnection);
        // Налаштування поведінки мок-об'єктів
//        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
//        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
//        when(mockResultSet.next()).thenReturn(true, false);
//        when(mockResultSet.getInt(anyString())).thenReturn(1, 2, 3); // Повернення довільних значень

        tableInventory = new TableInventory(mockConnection);
    }



    @Test
    void testGetRowCountFromProduct() throws SQLException {
        String countQuery = "SELECT COUNT(*) AS row_count FROM products";
        // Налаштування поведінки мок-об'єктів
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(countQuery)).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("row_count")).thenReturn(5);

        // Виклик методу, який потрібно протестувати
        int rowCount = tableInventory.getRowCountFromProduct();

        // Перевірка викликів методів
        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(countQuery);
        verify(mockResultSet, times(1)).next();
        verify(mockResultSet, times(1)).getInt("row_count");
        assertEquals(5, rowCount);
    }

    @Test
    void testGetRowCountFromStore() throws SQLException {

        String countQuery = "SELECT COUNT(*) AS row_count FROM store";
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(countQuery)).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("row_count")).thenReturn(3);


        int rowCount = tableInventory.getRowCountFromStore();


        verify(mockConnection, times(1)).createStatement();
        verify(mockStatement, times(1)).executeQuery(countQuery);
        verify(mockResultSet, times(1)).next();
        verify(mockResultSet, times(1)).getInt("row_count");


        assertEquals(3, rowCount);

    }
}