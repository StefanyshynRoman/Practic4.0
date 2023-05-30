package com.shpp.rstefanyshyn;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.activemq.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class ProductGenerator implements Constant {
    private static final Logger logger = LoggerFactory.getLogger(ProductGenerator.class);
    Connection connection;
    static Random random = new Random();
    AtomicInteger counter = new AtomicInteger();
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.getValidator();

    public ProductGenerator(Connection connection) {
        this.connection = connection;

    }
public void createProductStream() {
    int batchSize = Integer.parseInt(BATCH_SIZE);
    int numberOfProducts = Integer.parseInt(NUMBER_PRODUCTS);

    try {
        String insertQuery = "INSERT INTO products (type_id,name) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

        StopWatch stopWatch = new StopWatch();
        stopWatch.restart();

        int generatedProducts = 0;
        int validProducts = 0;

        while (validProducts < numberOfProducts) {
            Product product;
            try {
                product = new Product(generateType(), generateRandomProductName());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            Set<ConstraintViolation<Product>> violations = validator.validate(product);

            if (violations.isEmpty()) {
                try {
                    preparedStatement.setInt(1, product.getProductTypeId());
                    preparedStatement.setString(2, product.getProductName());
                    preparedStatement.addBatch();

                    if (generatedProducts % batchSize == 0) {
                        preparedStatement.executeBatch();
                        preparedStatement.clearBatch();
                    }

                    validProducts++;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            generatedProducts++;
        }

        preparedStatement.executeBatch();
        stopWatch.stop();
        logger.info("Generation products: {}", numberOfProducts);
        logger.info("Generation products is over: seconds - {}", stopWatch.taken() / THOUSAND);
        logger.warn("RPS - {}, Batch size - {}", 1000.0 * numberOfProducts / stopWatch.taken(), BATCH_SIZE);
        logger.warn("All product: {}, Product invalid: {} ", generatedProducts, generatedProducts - validProducts);

        preparedStatement.close();
    } catch (SQLException e) {
        logger.error("Product generator error: {}", e.getMessage());
    }
}

public int getRowCountFromProduct() throws SQLException {
    String countQuery = "SELECT COUNT(*) AS row_count FROM products";
    try (Statement statement = connection.createStatement()) {
        ResultSet resultSet = statement.executeQuery(countQuery);
        if (resultSet.next()) {
            return resultSet.getInt("row_count");
        }
    }
    return 0;
}
    public int getRowCountFromType() throws SQLException {
        String countQuery = "SELECT COUNT(*) AS row_count FROM type";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(countQuery);
            if (resultSet.next()) {
                return resultSet.getInt("row_count");
            }
        }
        return 0;
    }

    public int generateType() throws SQLException {
        return random.nextInt(getRowCountFromType()) + 1;

    }

    public  String generateRandomProductName() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int randomLimit = 5;
        Random randomProductName = new Random();
        int targetStringLength = randomProductName.nextInt(randomLimit) + randomLimit;

        return randomProductName.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}