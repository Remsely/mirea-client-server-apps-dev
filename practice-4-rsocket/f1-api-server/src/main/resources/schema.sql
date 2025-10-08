CREATE TABLE IF NOT EXISTS drivers
(
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    name   VARCHAR(255) NOT NULL UNIQUE,
    team   VARCHAR(255),
    number INT
);

CREATE TABLE IF NOT EXISTS lap_times
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    driver_id   BIGINT NOT NULL,
    lap_number  INT    NOT NULL,
    millis      BIGINT NOT NULL,
    recorded_at TIMESTAMP,
    CONSTRAINT fk_driver FOREIGN KEY (driver_id) REFERENCES drivers (id) ON DELETE CASCADE
);
