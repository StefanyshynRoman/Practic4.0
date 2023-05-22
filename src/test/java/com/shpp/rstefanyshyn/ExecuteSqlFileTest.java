package com.shpp.rstefanyshyn;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.Connection;
import java.sql.Statement;


import static org.mockito.Mockito.*;

public class ExecuteSqlFileTest {

    @Mock
    private Connection connection;

    @Mock
    private ExecuteSqlFile executeSqlFile;

    @Before
    public void setUp()  {
        executeSqlFile = new ExecuteSqlFile(connection);
    }
    @After
    public void tearDown()  {
        executeSqlFile = null;
    }
    @Test
    public void testReadDLL() throws Exception {
        MockitoAnnotations.openMocks(this);

        ExecuteSqlFile spyExecuteSqlFile = spy(new ExecuteSqlFile(connection));

        Statement mockStatement = mock(Statement.class);
       when(connection.createStatement()).thenReturn(mockStatement);
        spyExecuteSqlFile.readDLL();
        verify(mockStatement).execute(( "CREATE TABLE type(    id   SERIAL PRIMARY KEY,    type VARCHAR(255));"));

    }
}