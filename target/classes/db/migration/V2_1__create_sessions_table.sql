CREATE TABLE IF NOT EXISTS sessions (
    uuid INT,
    cookie VARCHAR(68),
    CONSTRAINT PK_sessions PRIMARY KEY (uuid)
);