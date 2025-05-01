ALTER TABLE users
    ADD role VARCHAR(255) NOT NULL default 'ROLE_USER';

INSERT INTO users (name, surname, email, role)
VALUES ('admin', 'admin', 'warshard1337@gmail.com', 'ROLE_ADMIN'),
       ('Георгий', 'Бог войны', 'zagorodnew.gosha@gmail.com', 'ROLE_ADMIN');