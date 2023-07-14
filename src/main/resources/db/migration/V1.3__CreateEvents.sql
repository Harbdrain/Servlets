CREATE TABLE fileserver.events (
	id INT NOT NULL UNIQUE AUTO_INCREMENT,
    user_id INT,
    file_id INT,
	PRIMARY KEY(id),
	FOREIGN KEY(user_id) REFERENCES fileserver.users(id),
	FOREIGN KEY(file_id) REFERENCES fileserver.files(id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;
