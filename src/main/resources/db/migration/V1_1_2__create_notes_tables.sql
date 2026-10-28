CREATE TABLE IF NOT EXISTS note
(
    id         uuid PRIMARY KEY,
    appuser_id uuid NOT NULL,
    title      text NOT NULL,
    content    text NOT NULL
);

CREATE INDEX IF NOT EXISTS note_appuser_id_idx ON note (appuser_id);