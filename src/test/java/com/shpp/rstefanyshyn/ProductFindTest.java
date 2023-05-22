package com.shpp.rstefanyshyn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

 class ProductFindTest {
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockStatement;
    @Mock
    private ResultSet mockResultSet;

    @BeforeEach
    public void setUp() throws SQLException {
        MockitoAnnotations.openMocks(this);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("address")).thenReturn("I-F, Franko str. ");
    }

    @Test
     void testFind() {
        try {
            ProductFind productFind = new ProductFind(mockConnection);
            productFind.find();

            // Перевірка, що методи були викликані з очікуваними параметрами
            verify(mockConnection).prepareStatement(anyString());
            verify(mockStatement).setString(eq(1), anyString());
            verify(mockStatement).executeQuery();
            verify(mockResultSet).next();
            verify(mockResultSet).getString("address");

            // Перевірка, що повідомлення про адресу магазину було виведено
            // (можна використовувати спеціальну бібліотеку логування для перевірки)
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}