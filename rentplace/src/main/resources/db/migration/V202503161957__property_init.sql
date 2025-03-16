CREATE TABLE properties
(
    property_id     BIGINT PRIMARY KEY NOT NULL,
    address         VARCHAR(255)          NOT NULL,
    description     TEXT,
    rating          FLOAT                 NOT NULL,
    cost_per_day    INTEGER               NOT NULL,
    area            FLOAT,
    bedrooms        INTEGER,
    sleeping_places INTEGER,
    bathrooms       INTEGER,
    max_guests      INTEGER
);