CREATE TABLE properties_facilities
(
    property_id BIGINT NOT NULL,
    facility_id BIGINT NOT NULL,
    PRIMARY KEY (property_id, facility_id),
    FOREIGN KEY (property_id) references properties (property_id) ON DELETE CASCADE,
    FOREIGN KEY (facility_id) references facilities (facility_id) ON DELETE CASCADE
);

CREATE INDEX idx_property_facility_property ON properties_facilities (property_id);
CREATE INDEX idx_property_facility_facility ON properties_facilities (facility_id);