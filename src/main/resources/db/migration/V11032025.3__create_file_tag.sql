create table file_tag
(
    file_id BIGINT REFERENCES file (id) NOT NULL,
    tag_id  BIGINT REFERENCES tag (id)  NOT NULL,
    PRIMARY KEY (file_id, tag_id)
);