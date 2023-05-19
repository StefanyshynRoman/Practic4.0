package com.shpp.rstefanyshyn;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;

public class GetProperty {

    private final String fileNameProperty;
    private static final Logger logger = LoggerFactory.getLogger(GetProperty.class);
    private final Properties prop = new Properties();
    String value;

    public GetProperty(String fileNameProperty) {
        this.fileNameProperty = fileNameProperty;
        loadFile();
    }

    private void loadFile() {
        try {
            InputStreamReader is = new InputStreamReader(
                    Objects.requireNonNull(App.class.getClassLoader().
                            getResourceAsStream(fileNameProperty)),
                    StandardCharsets.UTF_8);
            prop.load(is);
            logger.debug("Getting properties: {}",fileNameProperty);
        } catch (NullPointerException e) {
            logger.error("Error in program: file not found {}",fileNameProperty);
            System.exit(1);
            throw new RuntimeException("File hasn't found");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getValueFromProperty(String key) {
        logger.info(" Take from properties {}",key );
        return prop.getProperty(key.toLowerCase(Locale.ROOT));
    }
    public String getFileNameProperty() {
        logger.info("File name");
        return fileNameProperty;
    }

}