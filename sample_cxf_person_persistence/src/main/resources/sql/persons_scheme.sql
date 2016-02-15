CREATE DATABASE IF NOT EXISTS cxf_sample;
USE cxf_sample;
CREATE TABLE person
(
    id BIGINT(20) PRIMARY KEY NOT NULL,
    first_name VARCHAR(25) NOT NULL,
    last_name VARCHAR(25) NOT NULL,
    age INT(3) NOT NULL,
    birth_date DATE NOT NULL
);