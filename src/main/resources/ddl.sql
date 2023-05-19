DROP DATABASE IF EXISTS bdrom ;
DROP SCHEMA IF EXISTS bdrom CASCADE;
DROP TABLE IF EXISTS store CASCADE;
DROP TABLE IF EXISTS type CASCADE;
DROP TABLE  IF EXISTS products CASCADE;
DROP TABLE IF EXISTS finish CASCADE;


CREATE
    DATABASE bdrom;
CREATE
    SCHEMA bdrom;

CREATE TABLE store
(
    id      SERIAL PRIMARY KEY,

    address VARCHAR(255)


);

CREATE TABLE type

(
    id   SERIAL PRIMARY KEY,
    type VARCHAR(255)
);

CREATE TABLE products
(
    id      SERIAL PRIMARY KEY,

    type_id INT,

    name    VARCHAR(255),
    FOREIGN KEY (type_id) REFERENCES type (id)

);
CREATE TABLE finish
(
    id        SERIAL PRIMARY KEY,
    store_id   INT,
    product_id INT,
    qty        INT,
    FOREIGN KEY (store_id) REFERENCES store (id),
    FOREIGN KEY (product_id) REFERENCES products (id)

);