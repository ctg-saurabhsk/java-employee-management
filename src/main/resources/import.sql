CREATE TABLE IF NOT EXISTS employee (
    id BIGINT NOT NULL,
    emailid VARCHAR(255),
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    PRIMARY KEY (id)
) ENGINE=InnoDB;
