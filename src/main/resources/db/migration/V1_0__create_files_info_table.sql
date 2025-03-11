create sequence file_id_seq;
CREATE TABLE files_info (
    file_id BIGINT PRIMARY KEY NOT NULL DEFAULT NEXTVAL('file_id_seq'),
    file_name VARCHAR NOT NULL,
    file_owner_id VARCHAR NOT NULL,
    file_creation_date VARCHAR NOT NULL,
    file_last_modified_date VARCHAR NOT NULL,
    file_access_level VARCHAR NOT NULL,
    file_tags VARCHAR
)