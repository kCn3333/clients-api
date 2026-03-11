-- src/main/resources/schema-h2.sql (H2 syntax)
DROP TABLE IF EXISTS clients;
CREATE TABLE clients (
                         id         BIGINT PRIMARY KEY AUTO_INCREMENT,
                         first_name VARCHAR(45),
                         last_name  VARCHAR(45),
                         email      VARCHAR(45)
);

DROP TABLE IF EXISTS app_authorities;
DROP TABLE IF EXISTS app_users;

CREATE TABLE app_users (
                           username VARCHAR(50)  NOT NULL PRIMARY KEY,
                           password VARCHAR(100) NOT NULL,
                           enabled  BOOLEAN      NOT NULL
);

CREATE TABLE app_authorities (
                                 username  VARCHAR(50) NOT NULL,
                                 authority VARCHAR(50) NOT NULL,
                                 CONSTRAINT fk_app_authorities_user
                                     FOREIGN KEY (username) REFERENCES app_users (username)
);

CREATE UNIQUE INDEX ix_app_auth_username_authority
    ON app_authorities (username, authority);