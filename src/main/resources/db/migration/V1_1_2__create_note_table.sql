CREATE TABLE IF NOT EXISTS note
(
    appuser_id uuid NOT NULL,
    id         uuid NOT NULL,
    title      text NOT NULL,
    content    text NOT NULL,
    PRIMARY KEY (appuser_id, id)
);