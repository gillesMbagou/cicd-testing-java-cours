-- Création explicite des tables (optionnel mais recommandé)
CREATE TABLE IF NOT EXISTS app_users (
                                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                         username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
    );

CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id BIGINT NOT NULL,
                                          role VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES app_users(id)
    );

-- Insertion des données
INSERT INTO app_users (id, username, password) VALUES
                                                   (1, 'user', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6'),
                                                   (2, 'admin', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6');

INSERT INTO user_roles (user_id, role) VALUES
                                           (1, 'ROLE_USER'),
                                           (2, 'ROLE_ADMIN'),
                                           (2, 'ROLE_USER');