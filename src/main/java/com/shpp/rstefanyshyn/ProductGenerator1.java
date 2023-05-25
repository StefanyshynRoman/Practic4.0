package com.shpp.rstefanyshyn;



import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.apache.activemq.util.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class ProductGenerator1 implements Constant {
    private static final Logger logger = LoggerFactory.getLogger(ProductGenerator1.class);
    Connection connection;
    static Random random = new Random();
    AtomicInteger counter = new AtomicInteger();
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.getValidator();

    public ProductGenerator1(Connection connection) {
        this.connection = connection;

    }

    public void createProductStream() {
        int batchSize = Integer.parseInt(BATH_SIZE);
        int numberOfProducts = Integer.parseInt(NUMBER_PRODUCTS);

        try {
            String insertQuery = "INSERT INTO products (type_id, name) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            StopWatch stopWatch = new StopWatch();
            stopWatch.restart();

            int availableProcessors = Runtime.getRuntime().availableProcessors();
            logger.error("dsfafsfd {} ",availableProcessors);
            ExecutorService executorService = Executors.newFixedThreadPool(availableProcessors);
            List<Future<Void>> futures = new ArrayList<>();

            for (int i = 1; i <= numberOfProducts; i++) {
                int finalI = i;
                futures.add(executorService.submit(() -> {
                    int productType;
                    try {
                        productType = generateType();
                    } catch (SQLException e) {
                        logger.error("Generate type error", e);
                        return null;
                    }

                    String productName = generateRandomProductName();
                    Product product = new Product(productType, productName);
                    Set<ConstraintViolation<Product>> violations = validator.validate(product);

                    if (violations.isEmpty()) {
                        try {
                            preparedStatement.setInt(1, productType);
                            preparedStatement.setString(2, productName);
                            preparedStatement.addBatch();

                            if (finalI % batchSize == 0) {
                                preparedStatement.executeBatch();
                                preparedStatement.clearBatch();
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        counter.getAndIncrement();
                    }

                    return null;
                }));
            }

            // Wait for all tasks to complete
            for (Future<Void> future : futures) {
                try {
                    future.get();
                } catch (InterruptedException | ExecutionException e) {
                    logger.error("Task execution error", e);
                }
            }

            preparedStatement.executeBatch();
            stopWatch.stop();

            logger.info("Generation products: {}", numberOfProducts);
            logger.info("Generation products is over: seconds - {}", stopWatch.taken() / THOUSAND);
            logger.warn("RPS - {}, Batch size - {}", 1000.0 * numberOfProducts / stopWatch.taken(), BATH_SIZE);
            logger.warn("Product invalid: {}", counter);

            preparedStatement.close();

            executorService.shutdown();
        } catch (SQLException e) {
            logger.error("Product generator error: {}", e.getMessage());
        }
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