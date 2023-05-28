DROP DATABASE IF EXISTS rom1;
DROP SCHEMA IF EXISTS rom1 CASCADE;
DROP TABLE IF EXISTS store CASCADE;
DROP TABLE IF EXISTS type CASCADE;
DROP TABLE IF EXISTS products CASCADE;
DROP TABLE IF EXISTS inventory CASCADE;


CREATE
    DATABASE rom1;
CREATE
    SCHEMA rom1;

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

CREATE TABLE inventory
(
    id         SERIAL PRIMARY KEY,
    store_id   INT,
    product_id INT,
    qty        INT,
    FOREIGN KEY (store_id) REFERENCES store (id),
    FOREIGN KEY (product_id) REFERENCES products (id)

);

CREATE INDEX idx1 ON products (name);
CREATE INDEX idx11 ON products (id);
CREATE INDEX idx111 ON products (type_id);
CREATE INDEX idx2 ON inventory (product_id);
CREATE INDEX idx22 ON inventory (store_id);
CREATE INDEX idx222 ON inventory (qty);
CREATE INDEX idx3 ON type (id);
CREATE INDEX idx33 ON type (type);
CREATE INDEX idx4 ON store (id);
CREATE INDEX idx44 ON store (address);


