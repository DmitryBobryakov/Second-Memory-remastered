create table if not exists file_tag
(
    file_id bigint references file (id),
    tag_id  bigint references tag (id),
    primary key (file_id, tag_id)
);