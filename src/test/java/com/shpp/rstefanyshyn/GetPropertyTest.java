package com.shpp.rstefanyshyn;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import static org.junit.jupiter.api.Assertions.*;

class GetPropertyTest {
    private static final Logger logger = LoggerFactory.getLogger(GetPropertyTest.class);
    private GetProperty getProperty;

    @BeforeEach
    void before() {
        getProperty = new GetProperty("config.properties");
    }

    @Test
    void testGetValueFromProperty() {
        assertEquals("2", getProperty.getValueFromProperty("min"));
        assertEquals("2", getProperty.getValueFromProperty("MIN"));
        assertEquals("10", getProperty.getValueFromProperty("max"));
        assertEquals("3", getProperty.getValueFromProperty("inc"));
        logger.warn("testGetValueFromProperty+");

    }
    @Test
    void testNotGetValueFromProperty() {
        assertNotEquals("100", getProperty.getValueFromProperty("min"));
        assertNotEquals("100", getProperty.getValueFromProperty("max"));
        assertNotEquals("1.001", getProperty.getValueFromProperty("inc"));
        logger.warn("testNotGetValueFromProperty+");
    }
    @Test
    void getValueFromPropertyWithWrongKey() {
        assertNull(getProperty.getValueFromProperty("MAX_TEST"));
        logger.warn("getValueFromPropertyWithWrongKey+");
    }

    @Test
    void testGetFileNameProperty() {
        assertEquals("config.properties", getProperty.getFileNameProperty());
        logger.warn("testGetFileNameProperty+");
    }
    @Test
    void testNotGetFileNameProperty() {
        assertNotEquals("config.properties1", getProperty.getFileNameProperty());
        logger.warn("testNotGetFileNameProperty+");
    }


}
