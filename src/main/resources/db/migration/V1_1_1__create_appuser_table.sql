CREATE TABLE IF NOT EXISTS appuser
(
    id       uuid PRIMARY KEY,
    username text UNIQUE NOT NULL,
    password text        NOT NULL,
    scopes   smallint[]  NOT NULL
);