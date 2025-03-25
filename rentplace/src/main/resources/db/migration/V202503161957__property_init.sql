CREATE TABLE properties
(
    property_id     BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    address         VARCHAR(255) NOT NULL,
    description     VARCHAR(65535),
    rating          FLOAT        NOT NULL,
    cost_per_day    INTEGER      NOT NULL,
    area            FLOAT,
    bedrooms        INTEGER,
    sleeping_places INTEGER,
    bathrooms       INTEGER,
    max_guests      INTEGER
);