CREATE DATABASE IF NOT EXISTS hotel_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE hotel_db;

CREATE TABLE IF NOT EXISTS rooms (
    room_number     VARCHAR(20)    NOT NULL PRIMARY KEY,
    type            VARCHAR(20)    NOT NULL DEFAULT 'Single',
    price_per_night DECIMAL(10,2)  NOT NULL,
    status          VARCHAR(20)    NOT NULL DEFAULT 'Available',
    customer_name   VARCHAR(100)   DEFAULT '',
    customer_phone  VARCHAR(20)    DEFAULT '',
    number_of_nights INT           DEFAULT 0
);

SELECT 'Database setup complete!' AS status;
