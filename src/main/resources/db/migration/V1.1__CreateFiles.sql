CREATE TABLE fileserver.files (
    id INT NOT NULL UNIQUE AUTO_INCREMENT,
	name VARCHAR(255),
	`path` VARCHAR(255) NOT NULL UNIQUE,
	PRIMARY KEY(id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_unicode_ci;
