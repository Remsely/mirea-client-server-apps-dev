CREATE TABLE IF NOT EXISTS drivers
(
    id          BIGSERIAL PRIMARY KEY,
    first_name  VARCHAR(255)   NOT NULL,
    last_name   VARCHAR(255)   NOT NULL,
    number      INTEGER UNIQUE NOT NULL,
    nationality VARCHAR(255)   NOT NULL,
    team_id     BIGINT         NOT NULL
);
