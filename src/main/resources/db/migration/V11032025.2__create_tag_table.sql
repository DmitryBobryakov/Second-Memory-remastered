create sequence tag_id_seq;
create table tag
(
    id   bigint primary key not null default nextval('tag_id_seq'),
    name varchar(50)        not null
);
