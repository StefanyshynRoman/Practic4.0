package com.shpp.rstefanyshyn;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BDConnectionTest {
    @Mock
        private BDConnection bdConnection;

        @Mock
        private Connection connection;

        @Before
        public void setUp () {
            MockitoAnnotations.initMocks(this);
            bdConnection = new BDConnection();
        }

        @After
        public void tearDown() throws SQLException {
            bdConnection.disconnect();
        }

        @Test
        public void testConnect() throws SQLException {
            when(connection.isClosed()).thenReturn(false);


            assertNotNull("Connection should not be null", connection);
            assertFalse("Connection should be open", connection.isClosed());

            verify(connection, times(1)).isClosed();
        }

        @Test
        public void testDisconnect() throws SQLException {
            bdConnection.connect();
            assertFalse("is ok", bdConnection.disconnect());
            bdConnection.disconnect();
            connection.close();
            assertNotNull("Connection should be null after disconnect", connection);
        }
}
