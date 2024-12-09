-- User Table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);

-- Image Table
CREATE TABLE images (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    imgur_id VARCHAR(255) NOT NULL,
    link VARCHAR(255) NOT NULL,
    delete_hash VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);
