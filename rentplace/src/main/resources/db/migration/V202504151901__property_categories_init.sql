CREATE TABLE properties_categories
(
    property_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (property_id, category_id),
    FOREIGN KEY (property_id) references properties (property_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) references categories (category_id) ON DELETE CASCADE
);

CREATE INDEX idx_property_categories_property ON properties_categories (property_id);
CREATE INDEX idx_property_categories_category ON properties_categories (category_id);