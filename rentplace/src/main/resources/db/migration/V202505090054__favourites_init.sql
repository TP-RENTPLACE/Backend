create table favourites
(
    user_id     BIGINT NOT NULL,
    property_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, property_id),
    FOREIGN KEY (user_id) references users (user_id) ON DELETE CASCADE,
    FOREIGN KEY (property_id) references properties (property_id) ON DELETE CASCADE
);

CREATE INDEX idx_favourites_property ON favourites (property_id);
CREATE INDEX idx_favourites_user ON favourites (user_id);