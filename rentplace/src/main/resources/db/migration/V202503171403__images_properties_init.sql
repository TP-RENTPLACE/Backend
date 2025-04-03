CREATE TABLE properties_images(
    property_id BIGINT NOT NULL,
    image_id BIGINT NOT NULL,
    PRIMARY KEY (property_id, image_id),
    FOREIGN KEY (property_id) references properties(property_id) ON DELETE CASCADE,
    FOREIGN KEY (image_id) references images(image_id) ON DELETE CASCADE
);

CREATE INDEX idx_property_images_property ON properties_images(property_id);
CREATE INDEX idx_property_images_image ON properties_images(image_id);