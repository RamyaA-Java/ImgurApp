-- User Table Sample Data
INSERT INTO users (id, username, password) VALUES (1, 'john_doe', 'password123');
INSERT INTO users (id, username, password) VALUES (2, 'jane_smith', 'securepassword');

-- Image Table Sample Data
INSERT INTO images (id, imgur_id, link, delete_hash, user_id) VALUES 
(1, 'abc123', 'https://imgur.com/abc123', 'delete123', 1),
(2, 'def456', 'https://imgur.com/def456', 'delete456', 1),
(3, 'ghi789', 'https://imgur.com/ghi789', 'delete789', 2);
