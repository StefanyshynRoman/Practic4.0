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
import java.util.stream.IntStream;

public class ProductGenerator implements Constant {
    private static final Logger logger = LoggerFactory.getLogger(ProductGenerator.class);
    Connection connection;
    static Random random = new Random();
    AtomicInteger counter= new AtomicInteger();
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.getValidator();

    public ProductGenerator(Connection connection) {
        this.connection = connection;

    }



    public void createProductStream() {

        int batchSize= Integer.parseInt(BATH_SIZE);
        int numberOfProducts= Integer.parseInt(NUMBER_PRODUCTS);
        try {
            String insertQuery = "INSERT INTO products (type_id,name) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            StopWatch stopWatch=new StopWatch();
            stopWatch.restart();

                IntStream.rangeClosed(1, numberOfProducts)
                        .forEach(i -> {
                            int productType = 1;
                            try {
                                productType = generateType();
                            } catch (SQLException e) {
                                logger.error("generate type{}",e);
                                throw new RuntimeException(e);
                            }
                            String productName = generateRandomProductName();
                            Product product = new Product(productType, productName);
                            Set<ConstraintViolation<Product>> violations = validator.validate(product);
                            if (violations.isEmpty()) {
                            try {
                                preparedStatement.setInt(1, productType);
                               preparedStatement.setString(2, productName);
                              //  preparedStatement.setDouble(2, productPrice);
                                preparedStatement.addBatch();

                                if (i % batchSize == 0) {
                                    preparedStatement.executeBatch();
                                    preparedStatement.clearBatch();
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                            } } else {
                                counter.getAndIncrement();
                                }
                        });

                preparedStatement.executeBatch();
            stopWatch.stop();
            logger.info("Generation products :{} ", numberOfProducts);
            logger.info("Generation products is over: second - {} ", (stopWatch.taken()) / THOUSAND);

            preparedStatement.close();
            logger.warn("RPS- {} ",1000.0*numberOfProducts /stopWatch.taken()   );

            logger.warn("Product invalid  {} ", counter);
            } catch(SQLException e){
            logger.error("Product generator {} ", e.getMessage());

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
    public  int generateType() throws SQLException {
        return random.nextInt(getRowCountFromType()) + 1;

    }
    private static String generateRandomProductName() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int randomLimit = 5;
        Random random = new Random();
        int targetStringLength = random.nextInt(randomLimit) + randomLimit;

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }


    private static double generateRandomProductPrice() {
        return Math.round(random.nextDouble() * 10 * 100.0) / 100.0;

    }
}