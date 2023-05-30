# Practic4.0
Create tables with a DDL script, which will contain all the products of all Epicenter stores
Use JDBC to randomly generate at least 3 million products (in the sense of table rows)
Before inserting into the database, check the DTO for validity using hibernate-validator
Issue the address of the store with the largest number of products of a certain type,
and request the type of product from the console, 
using the command line parameter (for auto-start by Jenkins after the build, 
you can parameterize the job with the 'Product_type' parameter)
